-module(main).
-export([start/0]).

start()->
    Main = self(),
    spawn(fun()-> bot:initbot(Main) end),
    timer:sleep(1000),
    spawn(fun()-> human:inithumanplayer(Main) end),
    Dict = dict:new(),
    mainloop([], Dict).

mainloop(UserPIDs, MapDict) ->
    receive
	{register, PID,Coordinates} ->
	    io:format("Player ~p registered! ~n", [PID]),
	    MapDict2 = dict:store(Coordinates, PID, MapDict),
	    mainloop([PID | UserPIDs], MapDict2);

	{walk, GunmanPID, Direction} ->
	    {CoordinateX, CoordinateY} = getCoordinates2(GunmanPID, MapDict),
	    case Direction of
		["a"] ->
		    if CoordinateX<1 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs,MapDict);	    
		       true ->
			    FreeSpace = checkMap(MapDict,{CoordinateX-1,CoordinateY}),
			    if FreeSpace ->
				    GunmanPID ! {newposition, {CoordinateX-1,CoordinateY}},
				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
				    MapDict3 = dict:store({CoordinateX-1,CoordinateY}, GunmanPID, MapDict2),  
				    mainloop(UserPIDs,MapDict3);
			       true ->
				    io:format("Position ~p occupied! BATTLE! ~n", [{CoordinateX-1,CoordinateY}]),
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
				    mainloop(UserPIDs,MapDict)
			    end
		    end;
		["d"] ->
		    if CoordinateX>23 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict);
		       true ->
			    FreeSpace = checkMap(MapDict,{CoordinateX+1,CoordinateY}),
			    if FreeSpace ->
				    GunmanPID ! {newposition, {CoordinateX+1,CoordinateY}},
				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
				    MapDict3 = dict:store({CoordinateX+1,CoordinateY}, GunmanPID, MapDict2),  
				    mainloop(UserPIDs,MapDict3);
			       true ->
				    io:format("Position ~p occupied! BATTLE! ~n", [{CoordinateX+1,CoordinateY}]),
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
				    mainloop(UserPIDs,MapDict)			 
			    end
		    end;
		["s"] ->
		    if CoordinateY>23 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict);
		       true ->
			    FreeSpace = checkMap(MapDict,{CoordinateX,CoordinateY+1}),
			    if FreeSpace ->
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY+1}},
				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
				    MapDict3 = dict:store({CoordinateX,CoordinateY+1}, GunmanPID, MapDict2),  
				    mainloop(UserPIDs,MapDict3);
			       true ->
				    io:format("Position ~p occupied! BATTLE! ~n", [{CoordinateX,CoordinateY+1}]),
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
				    mainloop(UserPIDs,MapDict)			 
			    end
		    end;
		["w"] ->
		    if CoordinateY<1 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict);
		       true ->
			    FreeSpace = checkMap(MapDict,{CoordinateX,CoordinateY-1}),
			    if FreeSpace ->
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY-1}},
				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
				    MapDict3 = dict:store({CoordinateX,CoordinateY-1}, GunmanPID, MapDict2),  
				    mainloop(UserPIDs,MapDict3);
			       true ->
				    io:format("Position ~p occupied! BATTLE! ~n", [{CoordinateX,CoordinateY-1}]),
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
				    mainloop(UserPIDs,MapDict)			 
			    end
		    end;
		["Quit"] ->
		    exitall(UserPIDs);
		["quit"] ->
		    exitall(UserPIDs);
		_Else ->
		    io:format("Invalid input, try again! ~n"),	
		    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
		    mainloop(UserPIDs, MapDict)	      
	    end 
    end.

checkMap(MapDict, Coordinates) ->
    Map = dict:fetch_keys(MapDict),
    Result = lists:filter(fun(X) -> X == Coordinates end, Map),
    if Result == [] -> 
	    true;
       true ->
	    false
    end.
getCoordinates(_GunmanPID, [], _MapDict) ->
    {error, "No coordinates found!"};
getCoordinates(GunmanPID, [Head|Tail], MapDict) ->
    TempPID = dict:fetch(Head, MapDict),
    if(TempPID == GunmanPID) ->
	    Head;
      true ->
	    getCoordinates(GunmanPID, Tail, MapDict)
    end.

getCoordinates2(GunmanPID, MapDict) ->
    KeyList = dict:fetch_keys(MapDict),
    getCoordinates(GunmanPID, KeyList, MapDict).
    

    

    

exitall([]) ->
    timer:sleep(1000),
    io:format("All processes exited, now exiting main with pid ~p... Goodbye ~n",[self()]);
exitall([PID | Rest]) ->
    PID ! exit,
    exitall(Rest).

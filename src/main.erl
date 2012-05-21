-module(main).
-export([start/0]).

start()->
    Main = self(),
    {ok,Host} = inet:gethostname(),
    HostFull = string:concat("sigui@",Host),
    HostAtom = list_to_atom(HostFull),
    GUIPID = {gui,HostAtom},
    spawn(fun()-> bot:initbot(Main,GUIPID) end),
    timer:sleep(1000),
    spawn(fun()-> bot:initbot(Main,GUIPID) end),
    timer:sleep(1000),
    spawn(fun()-> human:inithumanplayer(Main,GUIPID) end),
    Dict = dict:new(),
    mainloop([], Dict,GUIPID).

mainloop(UserPIDs, MapDict,GUIPID) ->
    receive
	{register, PID,Coordinates} ->
	    io:format("Player ~p registered! ~n", [PID]),
	    MapDict2 = dict:store(Coordinates, PID, MapDict),
	    mainloop([PID | UserPIDs], MapDict2,GUIPID);

	{walk, GunmanPID, Direction} ->
	    {CoordinateX, CoordinateY} = getCoordinates2(GunmanPID, MapDict),
	    case Direction of
		["a"] ->
		    if CoordinateX<1 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs,MapDict,GUIPID);	    
		       true ->
			    {FreeSpace,OpponentPID} = checkMap(MapDict,{CoordinateX-1,CoordinateY}),
			    if FreeSpace ->
				    GunmanPID ! {newposition, {CoordinateX-1,CoordinateY}},
				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
				    MapDict3 = dict:store({CoordinateX-1,CoordinateY}, GunmanPID, MapDict2),  
				    mainloop(UserPIDs,MapDict3,GUIPID);
			       true -> %% START BATTLE
				    io:format("START BATTLE, ~p VS ~p, GUIPID: ~p~n",[GunmanPID,OpponentPID,GUIPID]),
				    battle(GunmanPID,OpponentPID,GUIPID),
				    io:format("BATTLE ENDED"),
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
				    mainloop(UserPIDs,MapDict,GUIPID)
			    end
		    end;
		["d"] ->
		    if CoordinateX>99 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict, GUIPID);
		       true ->
			    {FreeSpace,OpponentPID} = checkMap(MapDict,{CoordinateX+1,CoordinateY}),
			    if FreeSpace ->
				    GunmanPID ! {newposition, {CoordinateX+1,CoordinateY}},
				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
				    MapDict3 = dict:store({CoordinateX+1,CoordinateY}, GunmanPID, MapDict2),  
				    mainloop(UserPIDs,MapDict3, GUIPID);
			       true -> %% START BATTLE
				    io:format("START BATTLE, ~p VS ~p, GUIPID: ~p~n",[GunmanPID,OpponentPID,GUIPID]),
				    battle(GunmanPID,OpponentPID,GUIPID),
				    io:format("BATTLE ENDED"),
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
				    mainloop(UserPIDs,MapDict,GUIPID)		 
			    end
		    end;
		["s"] ->
		    if CoordinateY>99 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict, GUIPID);
		       true ->
			    {FreeSpace,OpponentPID} = checkMap(MapDict,{CoordinateX,CoordinateY+1}),
			    if FreeSpace ->
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY+1}},
				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
				    MapDict3 = dict:store({CoordinateX,CoordinateY+1}, GunmanPID, MapDict2),  
				    mainloop(UserPIDs,MapDict3, GUIPID);
			       true -> %% START BATTLE
				    io:format("START BATTLE, ~p VS ~p, GUIPID: ~p~n",[GunmanPID,OpponentPID,GUIPID]),
				    battle(GunmanPID,OpponentPID,GUIPID),
				    io:format("BATTLE ENDED"),
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
				    mainloop(UserPIDs,MapDict,GUIPID)			 
			    end
		    end;
		["w"] ->
		    if CoordinateY<1 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict, GUIPID);
		       true ->
			    {FreeSpace,OpponentPID} = checkMap(MapDict,{CoordinateX,CoordinateY-1}),
			    if FreeSpace ->
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY-1}},
				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
				    MapDict3 = dict:store({CoordinateX,CoordinateY-1}, GunmanPID, MapDict2),  
				    mainloop(UserPIDs,MapDict3, GUIPID);
			       true -> %% START BATTLE
				    io:format("START BATTLE, ~p VS ~p, GUIPID: ~p~n",[GunmanPID,OpponentPID,GUIPID]),
				    battle(GunmanPID,OpponentPID,GUIPID),
				    io:format("BATTLE ENDED"),
				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
				    mainloop(UserPIDs,MapDict,GUIPID)		 
			    end
		    end;
		exit ->
		    exitall(UserPIDs);
		["quit"] ->
		    exitall(UserPIDs);
		_Else ->
		    io:format("Invalid input, try again! ~n"),	
		    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
		    mainloop(UserPIDs, MapDict, GUIPID)	      
	    end 
    end.

checkMap(MapDict, Coordinates) ->
    Map = dict:fetch_keys(MapDict),
    Result = lists:filter(fun(X) -> X == Coordinates end, Map),
    if Result == [] -> 
	    {true, self()};
       true ->
	    {false, dict:fetch(Coordinates,MapDict)}
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

battle(GunmanPID,OpponentPID,GUIPID) ->
    io:format("FREEZING OPPONENT"),
    OpponentPID ! freeze,
    io:format("OPPONENT FROZEN!"),
    GUIPID ! {battle,bot,GunmanPID,OpponentPID,1337,42}, %% HÄR SKICKAS BATTLETUPELN TILL JAVA
    io:format("Occupied by ~p! BATTLE! ~n", [OpponentPID]).

exitall([]) ->
    timer:sleep(1000),
    io:format("All processes exited, now exiting main with pid ~p... Goodbye ~n",[self()]),
    halt();
exitall([PID | Rest]) ->
    PID ! exit,
    exitall(Rest).

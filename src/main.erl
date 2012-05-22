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
    MapDict = dict:new(),
    FrozenDict = dict:new(),
    mainloop([], MapDict,GUIPID,FrozenDict).

mainloop(UserPIDs, MapDict,GUIPID,FrozenDict) ->
    receive
	{register, PID,Coordinates} ->
	    io:format("Player ~p registered! ~n", [PID]),
	    MapDict2 = dict:store(Coordinates, PID, MapDict),
	    FrozenDict2 = dict:store(PID,false, FrozenDict),
	    mainloop([PID | UserPIDs], MapDict2,GUIPID,FrozenDict2);
	{unregister, PID} ->
	    MapDict2 = dict:erase(getCoordinates(PID,MapDict),MapDict),
	    FrozenDict2 = dict:erase(PID,FrozenDict),
	    UserPIDs2 = lists:delete(PID,UserPIDs),
	    mainloop(UserPIDs2,MapDict2,GUIPID,FrozenDict2);
	{unfreeze, PID} ->
	    FrozenDict2 = dict:store(PID,false,FrozenDict),
	    PID ! {newposition, getCoordinates(PID,MapDict)},
	    mainloop(UserPIDs,MapDict,GUIPID,FrozenDict2);
	{walk, GunmanPID, Direction} ->
	    {CoordinateX, CoordinateY} = getCoordinates(GunmanPID, MapDict),
	    case Direction of
		["a"] ->
		    if CoordinateX<1 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs,MapDict,GUIPID,FrozenDict);	    
		       true ->
			    OldCoordinates = {CoordinateX,CoordinateY},
			    NewCoordinates = {CoordinateX-1,CoordinateY},
			    walk(MapDict,OldCoordinates,NewCoordinates,GunmanPID,GUIPID,FrozenDict,UserPIDs)
		    end;
		["d"] ->
		    if CoordinateX>99 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict, GUIPID, FrozenDict);
		       true ->
			    OldCoordinates = {CoordinateX,CoordinateY},
			    NewCoordinates = {CoordinateX+1,CoordinateY},
			    walk(MapDict,OldCoordinates,NewCoordinates,GunmanPID,GUIPID,FrozenDict,UserPIDs)		 
		    end;
		["s"] ->
		    if CoordinateY>99 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict, GUIPID,FrozenDict);
		       true ->
			    OldCoordinates = {CoordinateX,CoordinateY},
			    NewCoordinates = {CoordinateX,CoordinateY+1},
			    walk(MapDict,OldCoordinates,NewCoordinates,GunmanPID,GUIPID,FrozenDict,UserPIDs)			 
		    end;
		["w"] ->
		    if CoordinateY<1 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict, GUIPID,FrozenDict);
		       true ->
			    OldCoordinates = {CoordinateX,CoordinateY},
			    NewCoordinates = {CoordinateX,CoordinateY-1},
			    walk(MapDict,OldCoordinates,NewCoordinates,GunmanPID,GUIPID,FrozenDict,UserPIDs)
		    end;
		exit ->
		    exitall(UserPIDs);
		["quit"] ->
		    exitall(UserPIDs);
		_Else ->
		    io:format("Invalid input, try again! ~n"),	
		    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
		    mainloop(UserPIDs, MapDict, GUIPID,FrozenDict)	      
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
getCoordinates2(_GunmanPID, [], _MapDict) ->
    {error, "No coordinates found!"};
getCoordinates2(GunmanPID, [Head|Tail], MapDict) ->
    TempPID = dict:fetch(Head, MapDict),
    if(TempPID == GunmanPID) ->
	    Head;
      true ->
	    getCoordinates2(GunmanPID, Tail, MapDict)
    end.

getCoordinates(GunmanPID, MapDict) ->
    KeyList = dict:fetch_keys(MapDict),
    getCoordinates2(GunmanPID, KeyList, MapDict).
isFrozen(PID, Dict) ->
    dict:fetch(PID, Dict).

battle(GunmanPID,OpponentPID,GUIPID) ->
    GUIPID ! {battle,bot,GunmanPID,OpponentPID,1337,42}, %% HÄR SKICKAS BATTLETUPELN TILL JAVA
    io:format("Occupied by ~p! BATTLE! ~n", [OpponentPID]).

exitall([]) ->
    timer:sleep(1000),
    io:format("All processes exited, now exiting main with pid ~p... Goodbye ~n",[self()]),
    halt();
exitall([PID | Rest]) ->
    PID ! exit,
    exitall(Rest).

walk(MapDict,OldCoordinates,NewCoordinates,GunmanPID,GUIPID,FrozenDict,UserPIDs) ->
    {FreeSpace,OpponentPID} = checkMap(MapDict,NewCoordinates),
    if FreeSpace ->
	    GunmanPID ! {newposition, NewCoordinates},
	    MapDict2 = dict:erase(OldCoordinates, MapDict),
	    MapDict3 = dict:store(NewCoordinates, GunmanPID, MapDict2),  
	    mainloop(UserPIDs,MapDict3,GUIPID, FrozenDict);
       true ->
	    io:format("COLLISION ~n"),
	    NotFrozen = not isFrozen(OpponentPID,FrozenDict),
	    io:format("(~p is not frozen) is ~p",[OpponentPID,NotFrozen]),
	    if(NotFrozen) ->  %% START BATTLE
		    io:format("FREEZING OPPONENT"),
		    OpponentPID ! freeze,
		    FrozenDict2 = dict:store(OpponentPID,true,FrozenDict),
		    GunmanPID ! freeze,
		    FrozenDict3 = dict:store(GunmanPID,true,FrozenDict2),
		    io:format("OPPONENT FROZEN!"),
		    io:format("START BATTLE, ~p VS ~p, GUIPID: ~p~n",[GunmanPID,OpponentPID,GUIPID]),
		    battle(GunmanPID,OpponentPID,GUIPID),
		    io:format("BATTLE ENDED"),
%		    GunmanPID ! {newposition, OldCoordinates},
		    mainloop(UserPIDs,MapDict,GUIPID, FrozenDict3);
	      true -> 
		    GunmanPID ! {newposition, OldCoordinates},
		    mainloop(UserPIDs,MapDict,GUIPID, FrozenDict)
	    end
       end.

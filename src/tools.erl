-module(tools).
-export([checkMap/3,getCoordinates/2,exitall/1,walk/7]).

checkMap(MapDict, {CoordinateX,CoordinateY},GunmanPID) ->
    Map = dict:fetch_keys(MapDict),
    Result = lists:filter(fun({X,Y}) -> ((((CoordinateX-4) < X) and (X < (CoordinateX+4))) and (((CoordinateY-6) < Y) and (Y < (CoordinateY+6))))  end, Map),
    if Result == [] -> 
	    {true, GunmanPID};
       true ->
	    Opponent = dict:fetch(lists:nth(1,Result),MapDict),
	    if Opponent == GunmanPID ->
		    {true, GunmanPID};
	       true ->
		    {false, Opponent}
	    end
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

exitall([]) ->
    timer:sleep(1000),
    io:format("All processes exited, now exiting main with pid ~p... Goodbye ~n",[self()]),
    halt();
exitall([PID | Rest]) ->
    PID ! exit,
    exitall(Rest).

walk(MapDict,OldCoordinates,NewCoordinates,GunmanPID,GUIPID,FrozenDict,UserPIDs) ->
    {FreeSpace,OpponentPID} = checkMap(MapDict,NewCoordinates,GunmanPID),
    if FreeSpace ->
	    GunmanPID ! {newposition, NewCoordinates},
	    MapDict2 = dict:erase(OldCoordinates, MapDict),
	    MapDict3 = dict:store(NewCoordinates, GunmanPID, MapDict2),  
	    mainloop:mainloop(UserPIDs,MapDict3,GUIPID, FrozenDict);
       true ->
	    io:format("COLLISION ~n ~p",[FreeSpace]),
	    Frozen = dict:fetch(OpponentPID,FrozenDict),
	    if(not Frozen) ->  %% START BATTLE
		    OpponentPID ! freeze,
		    FrozenDict2 = dict:store(OpponentPID,true,FrozenDict),
		    GunmanPID ! freeze,
		    FrozenDict3 = dict:store(GunmanPID,true,FrozenDict2),
		    GUIPID ! {battle,bot,GunmanPID,OpponentPID,1337,42}, %% HÄR SKICKAS BATTLETUPELN TILL JAVA
		    mainloop:mainloop(UserPIDs,MapDict,GUIPID, FrozenDict3);
	      true -> 
		    GunmanPID ! {newposition, OldCoordinates},
		    mainloop:mainloop(UserPIDs,MapDict,GUIPID, FrozenDict)
	    end
       end.

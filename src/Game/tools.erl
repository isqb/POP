-module(tools).
-export([checkMap/3,getCoordinates/2,exitall/1,walk/7]).

%%--------------------------------------------------------------------
%% @doc Checks the coordinates you want to go for nearby enemies,
%%      if no enemies has X-coordinates in the span (X-4,X+4) AND 
%%      Y-cordinates in the span (Y-6,Y+6) then {true, _} is returned,
%%      else {false, Opponent} is returned where Opponent is the PID of
%%      the enemy nearby
%% @spec checkMap(dict(),PlayerCoords::{X::int(),Y::int()},pid()) -> none()
%% @end
%%--------------------------------------------------------------------
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

%%--------------------------------------------------------------------
%% @doc A function to get the coordinates of GunmanPID, if this fails
%%      an error message {error, "No coordinates found!"} is returned
%% @spec getCoordinates(pid(),dict())-> {int(),int()}
%% @end
%%--------------------------------------------------------------------
getCoordinates(GunmanPID, MapDict) ->
    KeyList = dict:fetch_keys(MapDict),
    getCoordinates2(GunmanPID, KeyList, MapDict).
getCoordinates2(_GunmanPID, [], _MapDict) ->
    {error, "No coordinates found!"};
getCoordinates2(GunmanPID, [Head|Tail], MapDict) ->
    TempPID = dict:fetch(Head, MapDict),
    if(TempPID == GunmanPID) ->
	    Head;
      true ->
	    getCoordinates2(GunmanPID, Tail, MapDict)
    end.
%%--------------------------------------------------------------------
%% @doc Moves the player to the new coordinates if they are clear from
%%      nearby enemies, if the new coordinates isn't clear a message
%%      is sent to the GUI to start a battle
%% @spec walk(dict(),{int(),int()},{int(),int()},pid(),pid(),dict(),list()) -> none()
%% @end
%%--------------------------------------------------------------------
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
	    if(not Frozen) ->  
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
%%--------------------------------------------------------------------
%% @doc Kills all processes started from start:start()
%% @spec exitall(list()) -> none()
%% @end
%%--------------------------------------------------------------------
exitall([]) ->
    timer:sleep(1000),
    io:format("All child processes exited");
exitall([PID | Rest]) ->
    PID ! exit,
    exitall(Rest).

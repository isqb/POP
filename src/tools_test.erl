-module(tools_test).
-include_lib("eunit/include/eunit.hrl").

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%			   EUnit Test Cases                                  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% All functions with names ending wiht _test() or _test_() will be
%% called automatically by make test
checkMap_test() ->
    OwnPID = 1,
    OpponentPID = 2,
    MapDict = dict:new(),
    Result1 = tools:checkMap(MapDict,{13,13},OwnPID),
    MapDict2 = dict:store({13,13},OwnPID,MapDict),
    Result2 = tools:checkMap(MapDict2,{13,13},OwnPID),
    MapDict3 = dict:store({10,10},OpponentPID,MapDict),
    Result3 = tools:checkMap(MapDict3,{14,10},OwnPID),
    Result4 = tools:checkMap(MapDict3,{10,16},OwnPID),
    Result5 = tools:checkMap(MapDict3,{40,40},OwnPID),
    Result6 = tools:checkMap(MapDict3,{10,10},OwnPID),
    Result7 = tools:checkMap(MapDict3,{7,5},OwnPID),
    Result8 = tools:checkMap(MapDict3,{13,15},OwnPID),
    Result9 = tools:checkMap(MapDict3,{12,13},OwnPID),
    ?assertEqual({true,OwnPID},Result1),
    ?assertEqual({true,OwnPID},Result2),
    ?assertEqual({true,OwnPID},Result3),
    ?assertEqual({true,OwnPID},Result4),
    ?assertEqual({true,OwnPID},Result5),
    ?assertEqual({false,OpponentPID},Result6),
    ?assertEqual({false,OpponentPID},Result7),
    ?assertEqual({false,OpponentPID},Result8),
    ?assertEqual({false,OpponentPID},Result9).

getCoordinates_test() ->
    MapDict = dict:new(),
    GunmanPID = 1,
    Result1 = tools:getCoordinates(1,MapDict),
    MapDict2 = dict:store({1337,42},GunmanPID,MapDict),
    Result2 = tools:getCoordinates(1,MapDict2),
    ?assertEqual({error, "No coordinates found!"},Result1),
    ?assertEqual({1337,42},Result2).
    
walk(MapDict,OldCoordinates,NewCoordinates,GunmanPID,GUIPID,FrozenDict) ->
    {FreeSpace,OpponentPID} = tools:checkMap(MapDict,NewCoordinates,GunmanPID),
    if FreeSpace ->
	    GunmanPID ! {newposition, NewCoordinates},
	    MapDict2 = dict:erase(OldCoordinates, MapDict),
	    MapDict3 = dict:store(NewCoordinates, GunmanPID, MapDict2),
	    GunmanPID ! {dict, MapDict3};
       true ->
	    io:format("COLLISION ~n ~p",[FreeSpace]),
	    Frozen = dict:fetch(OpponentPID,FrozenDict),
	    if(not Frozen) ->  
		    OpponentPID ! freeze,
		    FrozenDict2 = dict:store(OpponentPID,true,FrozenDict),
		    GunmanPID ! freeze,
		    FrozenDict3 = dict:store(GunmanPID,true,FrozenDict2),
		    GUIPID ! {battle,bot,GunmanPID,OpponentPID,1337,42},
		    GUIPID ! {dict, FrozenDict3};
	      true -> 
		    GunmanPID ! {newposition, OldCoordinates}
	    end
       end.

opponent(TestPID) ->
    receive
	freeze ->
	    TestPID ! froze
    end.
	    

walk_test() ->
    MapDict = dict:new(),
    FrozenDict = dict:new(),
    OwnPID = self(),
    OpponentPID = spawn(fun()-> opponent(OwnPID) end),
    FrozenDict2 = dict:store(OpponentPID,false,FrozenDict),
    OldCoordinates = {0,0},
    NewCoordinates = {1,0},
    MapDict2 = dict:store(OldCoordinates,OwnPID,MapDict),
    MapDict3 = dict:store({40,40},OpponentPID,MapDict2),
    walk(MapDict3,OldCoordinates,NewCoordinates,OwnPID,OwnPID,FrozenDict),
    receive
	{newposition, Coordinates} ->
	    Result1 = Coordinates
    end,
    receive
	{dict, Dict} ->
	    ResultDict = Dict
    end,
    Result2 = dict:fetch_keys(ResultDict),
    NewCoordinates2 = {40,40},
    walk(MapDict3,OldCoordinates,NewCoordinates2,OwnPID,OwnPID,FrozenDict2),
    receive
	froze ->
	    Result3 = ok
    end,
    receive
	freeze ->
	    Result4 = ok
    end,
    receive
	{battle,bot,PID1,PID2,1337,42} ->
	    Result5 = {PID1,PID2}
    end,
    receive
	{dict, FrozenDict3} ->
	    Result6 = dict:fetch(OwnPID,FrozenDict3),
	    Result7 = dict:fetch(OpponentPID,FrozenDict3)
    end,
    FrozenDict4 = dict:store(OpponentPID,true,FrozenDict2),
    walk(MapDict3,OldCoordinates,NewCoordinates2,OwnPID,OwnPID,FrozenDict4),
    receive
	{newposition, Coordinates2} ->
	    Result8 = Coordinates2
    end,
    ?assertEqual(NewCoordinates,Result1),
    ?assertEqual([{40,40},NewCoordinates],Result2),
    ?assertEqual(ok,Result3),
    ?assertEqual(ok,Result4),
    ?assertEqual({OwnPID,OpponentPID},Result5),
    ?assertEqual(true,Result6),
    ?assertEqual(true,Result7),
    ?assertEqual(OldCoordinates,Result8).
	 
	

-module(main_test).
-include_lib("eunit/include/eunit.hrl").

%% % 14.06 sadasdasasd

%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% %%			   EUnit Test Cases                                     %%
%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% %% All functions with names ending wiht _test() or _test_() will be
%% %% called automatically by make test



mainloop(UserPIDs, MapDict,GUIPID,FrozenDict) ->
    receive
	{register, PID,Coordinates} ->
	    io:format("Player ~p registered! ~n", [PID]),
	    MapDict2 = dict:store(Coordinates, PID, MapDict),
	    GUIPID ! {regMap, MapDict2},
	    FrozenDict2 = dict:store(PID,false, FrozenDict),
	    GUIPID ! {regFreezeDict, FrozenDict2};
	{unregister, PID} ->
	    MapDict2 = dict:erase(tools:getCoordinates(PID,MapDict),MapDict),
	    GUIPID ! {unregMap, MapDict2},
	    FrozenDict2 = dict:erase(PID,FrozenDict),
	    GUIPID ! {unregFreezeDict, FrozenDict2},
	    UserPIDs2 = lists:delete(PID,UserPIDs),
	    GUIPID ! {unregPID, UserPIDs2};
	{unfreeze, PID} ->
	    FrozenDict2 = dict:store(PID,false,FrozenDict),
	    PID ! {newposition, tools:getCoordinates(PID,MapDict)},
	    GUIPID ! {unfreeze, FrozenDict2};
	{walk, GunmanPID, Direction} ->
	    {CoordinateX, CoordinateY} = tools:getCoordinates(GunmanPID, MapDict),
	    case Direction of
		["a"] ->
		    if CoordinateX<1 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}};	    
		       true -> %% walk(_, The old coordinates, The new coordinates,_m_,_,_)
			    tools:walk(MapDict,{CoordinateX,CoordinateY},{CoordinateX-1,CoordinateY},GunmanPID,GUIPID,FrozenDict,UserPIDs)
		    end;
		["d"] ->
		    if CoordinateX>99 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}};
		       true ->
			    tools:walk(MapDict,{CoordinateX,CoordinateY},{CoordinateX+1,CoordinateY},GunmanPID,GUIPID,FrozenDict,UserPIDs)
		    end;
		["s"] ->
		    if CoordinateY>99 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}};
		       true ->
			    tools:walk(MapDict,{CoordinateX,CoordinateY},{CoordinateX,CoordinateY+1},GunmanPID,GUIPID,FrozenDict,UserPIDs)
		    end;
		["w"] ->
		    if CoordinateY<1 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}};
		       true ->
			    tools:walk(MapDict,{CoordinateX,CoordinateY},{CoordinateX,CoordinateY-1},GunmanPID,GUIPID,FrozenDict,UserPIDs)
		    end
	    end;
	exit ->
	    tools:exitall(UserPIDs)
    end.

main_test()->
    MainPID = self(),
    MapDict = dict:new(),
    FrozenDict = dict:new(),
    LoopPID = spawn( fun() -> mainloop([], MapDict,MainPID,FrozenDict) end),
    LoopPID ! {register, MainPID,{42,42}},
    receive 
	{regMap, MapDict2} ->
	    Result1 = tools:getCoordinates(MainPID, MapDict2)
    end,
    receive 
	{regFreezeDict, FrozenDict2} ->
	    Result2 = dict:fetch(MainPID, FrozenDict2)
    end,
    FrozenDict3 = dict:store(MainPID,true,FrozenDict2),
    LoopPID2 = spawn( fun() -> mainloop([], MapDict2,MainPID,FrozenDict3) end),
    LoopPID2 ! {unfreeze, MainPID},
    receive
	{newposition, Coordinates} ->
	    Result3 = Coordinates
    end,
    receive 
	{unfreeze, FrozenDict4} ->
	    Result4 = dict:fetch(MainPID,FrozenDict4)
    end,
    LoopPID3 = spawn( fun() -> mainloop([], MapDict,MainPID,FrozenDict) end),
    LoopPID3 ! {unregister, MainPID},
    receive 
	{unregMap, MapDict3} ->
	    Result5 = dict:fetch_keys(MapDict3)
    end,
    receive
	{unregFreezeDict, FrozenDict5} ->
	    Result6 = dict:fetch_keys(FrozenDict5)
    end, 
    ?assertEqual({42,42},Result1),
    ?assertEqual(false,Result2),
    ?assertEqual({42,42},Result3),
    ?assertEqual(false,Result4),
    ?assertEqual([],Result5),
    ?assertEqual([],Result6).

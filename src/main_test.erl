-module(main_test).
 -include_lib("eunit/include/eunit.hrl").

%% % 14.06 sadasdasasd

%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% %%			   EUnit Test Cases                                     %%
%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% %% All functions with names ending wiht _test() or _test_() will be
%% %% called automatically by make test

fail_test()->
    ?assertEqual(1,0).

%% start_test()->
%% %%  spawn(fun() -> bot_test:init_test(Main) end),
%%     HmnPID = spawn(fun()-> human_test:init_test() end),
%%     Dict = dict:new(),
%%     io:format("DICT WHEN STARTED: ~p",[Dict]),
%%     mainloop(HmnPID, Dict).

%% mainloop(UserPIDs, MapDict) ->
%%     receive
%% 	{register, HmnPID,Coordinates} ->
%% 	    io:format("Player ~p registered! ~n", [HmnPID]),
%% 	    MapDict2 = dict:store(Coordinates, HmnPID, MapDict),
	    
%% 	    io:format("Player ~p registered in world: ~p", [HmnPID,dict:find(Coordinates, MapDict2)]),
	    
%% 	    mainloop([HmnPID | UserPIDs], MapDict2);

%% 	{walk, GunmanPID, Direction, {CoordinateX,CoordinateY}} ->
%% 	    case Direction of
%% 		["w"] ->
%% 		    if CoordinateX<1 ->
%% 			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
%% 			    mainloop(UserPIDs,MapDict);	    
%% 		       true ->
%% 			    FreeSpace = checkMap(MapDict,{CoordinateX-1,CoordinateY}),
%% 			    if FreeSpace ->
%% 				    GunmanPID ! {newposition, {CoordinateX-1,CoordinateY}},
%% 				    io:format("Before erase: ~p ~n", [MapDict]),
%% 				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
%% 				    io:format("After erase: ~p ~n", [MapDict2]),
%% 				    MapDict3 = dict:store({CoordinateX-1,CoordinateY}, GunmanPID, MapDict2),  
%% 				    io:format("After store: ~p ~n", [MapDict3]),
%% 				    mainloop(UserPIDs,MapDict3);
%% 			       true ->
%% 				    io:format("Dict before battle: ~p ~n", [MapDict]),
%% 				    io:format("Position ~p occupied! BATTLE! ~n", [{CoordinateX-1,CoordinateY}]),
%% 				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
%% 				    mainloop(UserPIDs,MapDict)
%% 			    end
%% 		    end;
%% 		["e"] ->
%% 		    if CoordinateX>99 ->
%% 			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
%% 			    mainloop(UserPIDs, MapDict);
%% 		       true ->
%% 			    FreeSpace = checkMap(MapDict,{CoordinateX+1,CoordinateY}),
%% 			    if FreeSpace ->
%% 				    GunmanPID ! {newposition, {CoordinateX+1,CoordinateY}},
%% 				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
%% 				    MapDict3 = dict:store({CoordinateX+1,CoordinateY}, GunmanPID, MapDict2),  
%% 				    mainloop(UserPIDs,MapDict3);
%% 			       true ->
%% 				    io:format("Dict before battle: ~p ~n", [MapDict]),
%% 				    io:format("Position ~p occupied! BATTLE! ~n", [{CoordinateX+1,CoordinateY}]),
%% 				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
%% 				    mainloop(UserPIDs,MapDict)			 
%% 			    end
%% 		    end;
%% 		["n"] ->
%% 		    if CoordinateY>99 ->
%% 			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
%% 			    mainloop(UserPIDs, MapDict);
%% 		       true ->
%% 			    FreeSpace = checkMap(MapDict,{CoordinateX,CoordinateY+1}),
%% 			    if FreeSpace ->
%% 				    GunmanPID ! {newposition, {CoordinateX,CoordinateY+1}},
%% 				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
%% 				    MapDict3 = dict:store({CoordinateX,CoordinateY+1}, GunmanPID, MapDict2),  
%% 				    mainloop(UserPIDs,MapDict3);
%% 			       true ->
%% 				    io:format("Dict before battle: ~p ~n", [MapDict]),
%% 				    io:format("Position ~p occupied! BATTLE! ~n", [{CoordinateX,CoordinateY+1}]),
%% 				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
%% 				    mainloop(UserPIDs,MapDict)			 
%% 			    end
%% 		    end;
%% 		["s"] ->
%% 		    if CoordinateY<1 ->
%% 			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
%% 			    mainloop(UserPIDs, MapDict);
%% 		       true ->
%% 			    FreeSpace = checkMap(MapDict,{CoordinateX,CoordinateY-1}),
%% 			    if FreeSpace ->
%% 				    GunmanPID ! {newposition, {CoordinateX,CoordinateY-1}},
%% 				    MapDict2 = dict:erase({CoordinateX,CoordinateY}, MapDict),
%% 				    MapDict3 = dict:store({CoordinateX,CoordinateY-1}, GunmanPID, MapDict2),  
%% 				    mainloop(UserPIDs,MapDict3);
%% 			       true ->
%% 				    io:format("Dict before battle: ~p ~n", [MapDict]),
%% 				    io:format("Position ~p occupied! BATTLE! ~n", [{CoordinateX,CoordinateY-1}]),
%% 				    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
%% 				    mainloop(UserPIDs,MapDict)			 
%% 			    end
%% 		    end;
%% 		["Quit"] ->
%% 		    exitall(UserPIDs);
%% 		["quit"] ->
%% 		    exitall(UserPIDs);
%% 		_Else ->
%% 		    io:format("Invalid input, try again! ~n"),	
%% 		    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
%% 		    mainloop(UserPIDs, MapDict)	      
%% 	    end,
%% 	    io:format("PIDS: ~p ~n",[UserPIDs]),
%% 	    Poss = dict:fetch(UserPIDs,MapDict),
%% 	    io:format("Poss: ~p ~n",[Poss])
%% %%	    ?assertEqual({11,0},Poss),
%%     end.

%% checkMap(MapDict, Coordinates) ->
%% 	Map = dict:fetch_keys(MapDict),
%% 	Result = lists:filter(fun(X) -> X == Coordinates end, Map),
%% 	if Result == [] -> 
%% 		true;
%% 		true ->
%% 		false
%% 	end.
		  
%% exitall([]) ->
%%     timer:sleep(1000),
%%     io:format("All processes exited, now exiting main with pid ~p... Goodbye ~n",[self()]);
%% exitall([PID | Rest]) ->
%%     PID ! exit,
%%     exitall(Rest).

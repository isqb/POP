-module(human_test).
-include_lib("eunit/include/eunit.hrl").

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%			   EUnit Test Cases                                  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% All functions with names ending wiht _test() or _test_() will be
%% called automatically by make test
inithumanplayer_test() ->
    Main = self(),
    HumanPID = spawn(fun()-> human:inithuman(Main,Main) end), 		  
    receive
	{register,HumanPID, _Irrelevant} ->
	    io:format("Got registration"),
	    Result1 = success
    end,
    receive
	{move,human,HumanPID,HumanPID,_Irrelevant2,_Irrelevant3} ->
	    io:format("Got move"),
	    Result2 = success
    end,
    HumanPID ! exit,
    ?assertEqual(success,Result1),
    ?assertEqual(success,Result2).

humanloop_test() ->
    Main = self(),
    HumanPID = spawn(fun()-> human:inithuman(Main,Main) end), 		  
    receive
	{register,HumanPID, _Irrelevant} ->
	    ignore
    end,
    receive
	{move,human,HumanPID,HumanPID,_Irrelevant2,_Irrelevant3} ->
	    ignore
    end,
    HumanPID ! {newposition, {1337,42}},
    receive
	{move,human,HumanPID,HumanPID,CoordX,CoordY} ->
	    Result1 = {CoordX,CoordY}
    end,
    HumanPID ! {move,w},
    receive
	{walk,HumanPID, DirectionW} ->
	    Result2 = DirectionW
    end,   
    HumanPID ! {move,a},
    receive
	{walk,HumanPID, DirectionA} ->
	    Result3 = DirectionA
    end,
    HumanPID ! {move,s},
    receive
	{walk,HumanPID, DirectionS} ->
	    Result4 = DirectionS
    end,
    HumanPID ! {move,d},
    receive
	{walk,HumanPID, DirectionD} ->
	    Result5 = DirectionD
    end,
    HumanPID ! freeze,
    HumanPID ! unfreeze,
    receive
	{unfreeze, HumanPID} ->
	    Result6 = unfroze
    end,
    HumanPID ! freeze,
    HumanPID ! kill,
    receive
	{unregister, HumanPID} ->
	    Result7 = killed
    end,
    HumanPID ! exit,
    ?assertEqual({1337,42},Result1),
    ?assertEqual(["w"],Result2),
    ?assertEqual(["a"],Result3),
    ?assertEqual(["s"],Result4),
    ?assertEqual(["d"],Result5),
    ?assertEqual(unfroze,Result6),
    ?assertEqual(killed,Result7).

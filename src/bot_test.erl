-module(bot_test).
-include_lib("eunit/include/eunit.hrl").

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%			   EUnit Test Cases                                  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% All functions with names ending wiht _test() or _test_() will be
%% called automatically by make test

init_test() ->
    Main = self(),
    InitPID = spawn(fun() -> bot:initbot(Main) end), %%Spawn a bot for testing
    receive
	{register, InitPID,_Coordinates} ->
	    Success = true
    end,
    exit(InitPID,normal),
    ?assertEqual(true,Success).

loop_test() ->
    Main = self(),
    LoopPID = spawn(fun() -> bot:initbot(Main) end), %%Spawn a botloop process for testing
    LoopPID ! {newposition, {10,1}},
    timer:sleep(100),
    receive 
<<<<<<< HEAD
	{walk, LoopPID, Direction1, Coordinates1} ->
=======
	{walk, LoopPID, _Direction1, Coordinates1} ->
>>>>>>> d23e04e7561ac731a9f3b2092b8499ee86bf239b
	    io:format("First walk: ~p~n",[Coordinates1]),
	    Result1 = Coordinates1	
    end,
    receive 
<<<<<<< HEAD
	{walk, LoopPID, Direction2, Coordinates2} ->
=======
	{walk, LoopPID, _Direction2, Coordinates2} ->
>>>>>>> d23e04e7561ac731a9f3b2092b8499ee86bf239b
	    io:format("Second walk: ~p~n",[Coordinates2]),
	    Result2 = Coordinates2
    end,
    io:format("~n~n RESULT = ~p ~n ~n",[Result2]),
    exit(LoopPID,normal),
    ?assertEqual({{10,0},{10,1}},{Result1,Result2}).

    
    

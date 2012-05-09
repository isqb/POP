-module(bot_test).
-include_lib("eunit/include/eunit.hrl").

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%			   EUnit Test Cases                                  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% All functions with names ending wiht _test() or _test_() will be
%% called automatically by make test

aborttest(Main) ->
    timer:sleep(10000),
    Main ! abort.

init_test() ->
    Main = self(),
    spawn(fun() -> bot:initbot(Main) end), %%Spawn a bot for testing
    spawn(fun() -> aborttest(Main) end), %%Spawn a process to abort this test and fail after timeout
    receive
	{register, _PID,_Coordinates} ->
	    Success = true;
	abort ->
	    Success = false
    end,
    ?assertEqual(Success,true).

loop_test() ->
    Main = self(),
    spawn(fun() -> aborttest(Main) end), %%Spawn a process to abort this test and fail after timeout
    LoopPID = spawn(fun() -> bot:initbot(Main) end), %%Spawn a botloop process for testing
    LoopPID ! {newposition, {10,1}},
    timer:sleep(100),
    receive 
	{walk, PID, Direction, Coordinates} ->
	    Result = Coordinates;
	abort ->
	    Result = {0,0}
    end,
    io:format("~n~n RESULT = ~p ~n ~n",[Result]), 
    ?assertEqual({10,1},Result).

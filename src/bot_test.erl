-module(bot_test).
-include_lib("eunit/include/eunit.hrl").

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% EUnit Test Cases %%
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
    receive
	{walk, LoopPID, _Direction1, Coordinates1} ->
	    io:format("")
    end,
    receive
	{walk, LoopPID, _Direction2, Coordinates2} ->
	    Result2 = Coordinates2
    end,
    exit(LoopPID,normal),
    ?assertEqual({10,1},Result2).


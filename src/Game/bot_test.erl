-module(bot_test).
-include_lib("eunit/include/eunit.hrl").

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% EUnit Test Cases %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% All functions with names ending wiht _test() or _test_() will be
%% called automatically by make test

init_test() ->
    Main = self(),
    BotPID = spawn(fun() -> bot:initbot(Main,Main) end), %%Spawn a bot for testing
    receive
	{register, BotPID,_Coordinates} ->
	    Success = true
    end,
    receive 
	{walk, BotPID, _Direction} ->
	    Success2 = true
    end,
    BotPID ! exit,
    ?assertEqual(true,Success2), 
    ?assertEqual(true,Success).

loop_test() ->
    Main = self(),
    BotPID = spawn(fun() -> bot:initbot(Main,Main) end), %%Spawn a botloop process for testing
    BotPID ! {newposition, {10,1}},
    receive
	{walk, BotPID, Direction} ->
	    DirectionList = [["w"],["a"],["s"],["d"]],
	    Result = lists:filter(fun(X) -> X == Direction end, DirectionList),
	    if 
		Result == [] ->
		    Success3 = false;
		true ->
		    Success3 = true
	    end
    end,
    BotPID ! freeze,
    BotPID ! unfreeze,
    receive 
	{unfreeze,BotPID} ->
	    Success4 = true
    end,
    
    BotPID ! freeze,
    BotPID ! kill,
    receive 
	{unregister,botdeath, BotPID} ->
	    Success5 = true
    end,

    BotPID ! exit,

    ?assertEqual(true,Success3),
    ?assertEqual(true,Success4),
    ?assertEqual(true,Success5).


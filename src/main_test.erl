-module(main_test).
 -include_lib("eunit/include/eunit.hrl").

%% % 14.06 sadasdasasd

%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% %%			   EUnit Test Cases                                     %%
%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% %% All functions with names ending wiht _test() or _test_() will be
%% %% called automatically by make test

start_test()->
    main:start(),
    receive
	{register,_PID1,_Coordinates1} ->
	    io:format("FIRST"),
	    Result1 = firstregistered
    end,
    receive
	{register, _PID2,_Coordinates2} ->
	    io:format("SECOND"),
	    Result2 = secondregistered
    end,
    ?assertEqual({firstregistered,secondregistered},{Result1,Result2}).

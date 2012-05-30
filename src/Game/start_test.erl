-module(start_test).
-include_lib("eunit/include/eunit.hrl").

%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% %%			   EUnit Test Cases                                     %%
%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% %% All functions with names ending wiht _test() or _test_() will be
%% %% called automatically by make test

start_test()->
    Main = self(),
    {ok,Host} = inet:gethostname(),
    HostFull = string:concat("sigui@",Host),
    HostAtom = list_to_atom(HostFull),
    GUIPID = {gui,HostAtom},
    GUIPID ! self(), %% send MainPID to GUI 
    spawn(fun()-> bot:initbot(Main,GUIPID) end),
    spawn(fun()-> human:inithuman(Main,GUIPID) end),
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

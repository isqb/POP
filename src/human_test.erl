-module(human_test).
-include_lib("eunit/include/eunit.hrl").

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%			   EUnit Test Cases                                  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% All functions with names ending wiht _test() or _test_() will be
%% called automatically by make test

fail_test()->
    ?assertEqual(1,0).

%% init_test() ->
%%     Main = self(),
%%     InitPID = spawn(fun() -> inithumanplayer_test(Main) end),
%%     receive 
%% 	{register, InitPID, Coordinates} ->
%% 	    Success = true,
%% 	    Cord = Coordinates
%%     end,
%%     receive
%% 	{walk, InitPID, Direction, _Newcoordinates} ->
%% 	    Result = Direction
%%     end,
%%     exit(InitPID,normal),
%%     ?assertEqual(true,Success),
%%     ?assertEqual({0,0},Cord),
%%     ?assertEqual(["w"], Result).

%% inithumanplayer_test(MainPID) ->
%%     random:seed(now()),
%%     Coordinates ={0,0},
%%     MainPID ! {register, self(),Coordinates},
%%     GunmanPID = self(),
%%     InputPID = spawn(fun() -> walk(MainPID,GunmanPID) end), 
%%     InputPID ! {walk, Coordinates}.

%% walk(MainPID,GunmanPID) ->
%%     receive
%% 	{walk, Newcoordinates} ->
%% 	    io:format("~p~n",[{"You are now at: ", self(), Newcoordinates}]),
%% 	    {ok, Binary} = file:read_file("/home/olbj3883/Desktop/pop_2012_project_group_01/src/human_test_cord.txt"),
%% 	    Direction = string:tokens(erlang:binary_to_list(Binary), "\n"),
%% 	    io:format("Cords: ~p ~n" , [Direction]),
%% 	    MainPID ! {walk, GunmanPID, Direction, Newcoordinates},
%% 	    walk(MainPID,GunmanPID);
%% 	exit ->
%% 	    io:format("Input process with PID ~p exited~n",[self()])
%%     end.

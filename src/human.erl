-module(human).
-export([inithumanplayer/1]).

inithumanplayer(MainPID) ->
    random:seed(now()),
    {CoordinateX,CoordinateY} = {random:uniform(536),random:uniform(897)},
    MainPID ! {register, self(), {CoordinateX,CoordinateY}},
    %% InputPID = spawn(fun() -> walk(MainPID,GunmanPID) end), 
    %% InputPID ! {walk, Coordinates},
    {ok,Host} = inet:gethostname(),
    HostFull = string:concat("sigui@",Host),
    HostAtom = list_to_atom(HostFull),
    {gui,HostAtom} ! {human,self(),CoordinateX,CoordinateY},
    humanloop(MainPID).

%% walk(MainPID,GunmanPID) ->
%%     receive
%% 	{walk, Newcoordinates} -> 
%% 	    io:format("~p~n",[{"Where do you want to go now...", self(), Newcoordinates}]),
%% 	    {ok, Direction} = 
%% 	    MainPID ! {walk, GunmanPID, Direction, Newcoordinates},
%% 	    walk(MainPID,GunmanPID);
%% 	exit ->
%% 	    io:format("Input process with PID ~p exited~n",[self()])
%%     end.

humanloop(MainPID) ->
    receive
	{newposition, {CoordinateX,CoordinateY}} ->
	    Node = node(),
	    io:format("Node: ~p ~n",[Node]),
	    {ok,Host} = inet:gethostname(),
	    HostFull = string:concat("sigui@",Host),
	    HostAtom = list_to_atom(HostFull),
	    {gui,HostAtom} ! {human,self(),CoordinateX,CoordinateY},
	    humanloop(MainPID);
	{move,w} -> 
    	    MainPID ! {walk, self(), ["w"]},
	    humanloop(MainPID);
	{move,a} -> 
    	    MainPID ! {walk, self(), ["a"]},
	    humanloop(MainPID);
	{move,s} -> 
    	    MainPID ! {walk, self(), ["s"]},
	    humanloop(MainPID);
	{move,d} -> 
    	    MainPID ! {walk, self(), ["d"]},
	    humanloop(MainPID);
	exit ->
	    io:format("Gunmanloop with pid ~p exited~n",[self()]);
	close ->
	    MainPID ! {walk, self(), exit}
    end.

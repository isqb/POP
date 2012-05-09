-module(human).
-export([inithumanplayer/1]).

inithumanplayer(MainPID) ->
    random:seed(now()),
    Coordinates = {random:uniform(24),random:uniform(24)},
    MainPID ! {register, self(),Coordinates},
    GunmanPID = self(),
    InputPID = spawn(fun() -> walk(MainPID,GunmanPID) end), 
    InputPID ! {walk, Coordinates},
    humanloop(MainPID,InputPID).

walk(MainPID,GunmanPID) ->
    receive
	{walk, Newcoordinates} ->
	    io:format("~p~n",[{"Where do you want to go now...", self(), Newcoordinates}]),
	    {ok, Direction} = io:fread("...East,West,North or South?    ","~s"),
	    MainPID ! {walk, GunmanPID, Direction, Newcoordinates},
	    
	    walk(MainPID,GunmanPID);
	exit ->
	    io:format("Input process with PID ~p exited~n",[self()])
    end.

humanloop(MainPID,InputPID) ->
    receive
	{newposition, {CoordinateX,CoordinateY}} ->
	    InputPID ! {walk, {CoordinateX,CoordinateY}},
	    {gui,'sigui@sernander'} ! {self(),CoordinateX,CoordinateY},
	    humanloop(MainPID,InputPID);
	exit ->
	    InputPID ! exit,
	    io:format("Gunmanloop with pid ~p exited~n",[self()])
    end.

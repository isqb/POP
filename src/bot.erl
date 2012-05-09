-module(bot).
-export([initbot/1, botloop/1]).

initbot(MainPID) ->
    random:seed(now()),
    Coordinates = {10,0},
    MainPID ! {register, self(),Coordinates},
    random:seed(now()),
    Direction = lists:nth(random:uniform(4),[["w"],["e"],["n"],["s"]]),
    MainPID ! {walk, self(), Direction, Coordinates},
    botloop(MainPID).

botloop(MainPID) ->
    receive
	{newposition, {CoordinateX,CoordinateY}} ->
	    io:format("BOT: ~p ~n", [{CoordinateX,CoordinateY, self()}]),
	    {gui,'sigui@sernander'} ! {self(),CoordinateX,CoordinateY},
	    timer:sleep(1000),
	    random:seed(now()),
	    Direction = lists:nth(random:uniform(4),[["w"],["e"],["n"],["s"]]),
	    MainPID ! {walk, self(), Direction, {CoordinateX,CoordinateY}},
	    botloop(MainPID);
	exit ->
	    io:format("Botloop with PID ~p exited~n",[self()])
    end.

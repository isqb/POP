-module(bot).
-export([initbot/1, botloop/1]).

initbot(MainPID) ->
    random:seed(now()),
    Coordinates = {random:uniform(24),random:uniform(24)},
    MainPID ! {register, self(),Coordinates},
    random:seed(now()),
    Direction = lists:nth(random:uniform(4),[["w"],["a"],["d"],["s"]]),
    MainPID ! {walk, self(), Direction, Coordinates},
    botloop(MainPID).

botloop(MainPID) ->
    receive
	{newposition, {CoordinateX,CoordinateY}} ->
	    io:format("BOT: ~p ~n", [{CoordinateX,CoordinateY, self()}]),
	    {gui,'sigui@sernander'} ! {self(),CoordinateX,CoordinateY},
	    timer:sleep(50),
	    random:seed(now()),
	    Direction = lists:nth(random:uniform(4),[["w"],["a"],["d"],["s"]]),
	    MainPID ! {walk, self(), Direction, {CoordinateX,CoordinateY}},
	    botloop(MainPID);
	exit ->
	    io:format("Botloop with PID ~p exited~n",[self()])
    end.

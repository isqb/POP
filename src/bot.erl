-module(bot).
-export([initbot/2, botloop/2]).

initbot(MainPID, GUIPID) ->
    random:seed(now()),
    Coordinates = {random:uniform(99),random:uniform(99)},
    MainPID ! {register, self(),Coordinates},
    random:seed(now()),
    Direction = lists:nth(random:uniform(4),[["w"],["a"],["d"],["s"]]),
    MainPID ! {walk, self(), Direction},
    botloop(MainPID, GUIPID).

botloop(MainPID, GUIPID) ->
    receive
	{newposition, {CoordinateX,CoordinateY}} ->
	    io:format("BOT: ~p ~n", [{CoordinateX,CoordinateY, self()}]),
	    GUIPID ! {move,bot,self(),self(),CoordinateX,CoordinateY},
	    timer:sleep(500),
	    random:seed(now()),
	    Direction = lists:nth(random:uniform(4),[["w"],["a"],["d"],["s"]]),
	    MainPID ! {walk, self(), Direction},
	    botloop(MainPID, GUIPID);
	freeze ->
	    receive
		unfreeze ->
		    io:format("I'm unfrozen!");
		die ->
		    MainPID ! {unregister, self()},
		    io:format("I'm dead")
	    end,
	    botloop(MainPID, GUIPID);
	exit ->
	    io:format("Botloop with PID ~p exited~n",[self()])
    end.

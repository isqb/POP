-module(bot).
-export([initbot/1, botloop/1]).

initbot(MainPID) ->
    random:seed(now()),
    Coordinates = {random:uniform(24),random:uniform(24)},
    MainPID ! {register, self(),Coordinates},
    random:seed(now()),
    Direction = lists:nth(random:uniform(4),[["w"],["a"],["d"],["s"]]),
    MainPID ! {walk, self(), Direction},
    botloop(MainPID).

botloop(MainPID) ->
    receive
	{newposition, {CoordinateX,CoordinateY}} ->
	    io:format("BOT: ~p ~n", [{CoordinateX,CoordinateY, self()}]),
	    {ok,Host} = inet:gethostname(),
	    HostFull = string:concat("sigui@",Host),
	    HostAtom = list_to_atom(HostFull),
	    {gui,HostAtom} ! {bot,self(),CoordinateX,CoordinateY},
	    timer:sleep(50),
	    random:seed(now()),
	    Direction = lists:nth(random:uniform(4),[["w"],["a"],["d"],["s"]]),
	    MainPID ! {walk, self(), Direction},
	    botloop(MainPID);
	exit ->
	    io:format("Botloop with PID ~p exited~n",[self()])
    end.

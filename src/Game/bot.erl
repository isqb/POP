-module(bot).
-export([initbot/2, botloop/2]).

%%--------------------------------------------------------------------
%% @doc Creates a computer-controlled player, a "bot", to walk the
%% plains of Sudden Impact and cause havoc. 
%% @spec initbot(pid(),pid()) -> none()
%% @end
%%--------------------------------------------------------------------
initbot(MainPID, GUIPID) ->
    random:seed(now()),
    Coordinates = {random:uniform(99),random:uniform(99)},
    MainPID ! {register, self(),Coordinates},
    random:seed(now()),
    Direction = lists:nth(random:uniform(4),[["w"],["a"],["d"],["s"]]),
    MainPID ! {walk, self(), Direction},
    botloop(MainPID, GUIPID).

%%--------------------------------------------------------------------
%% @doc Process for bots to listen for commands and signals. 
%% @spec botloop(pid(),pid()) -> none()
%% @end
%%--------------------------------------------------------------------
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
		    io:format("I'm unfrozen!"),
		    MainPID ! {unfreeze, self()},
		    botloop(MainPID,GUIPID);
		kill ->
		    MainPID ! {unregister, botdeath, self()},
		    io:format("I'm dead (bot)")
	    end;
	exit ->
	    io:format("Botloop with PID ~p exited~n",[self()])
    end.


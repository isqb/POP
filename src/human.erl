-module(human).
-export([inithumanplayer/2]).

inithumanplayer(MainPID, GUIPID) ->
    random:seed(now()),
    {CoordinateX,CoordinateY} = {random:uniform(99),random:uniform(99)},
    MainPID ! {register, self(), {CoordinateX,CoordinateY}},
    %% InputPID = spawn(fun() -> walk(MainPID,GunmanPID) end), 
    %% InputPID ! {walk, Coordinates},
    GUIPID ! {move,human,self(),self(),CoordinateX,CoordinateY},
    humanloop(MainPID, GUIPID).

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

humanloop(MainPID, GUIPID) ->
    receive
	{newposition, {CoordinateX,CoordinateY}} ->
	    Node = node(),
	    io:format("Node: ~p ~n",[Node]),
	    GUIPID ! {move,human,self(),self(),CoordinateX,CoordinateY},
	    humanloop(MainPID, GUIPID);
	{move,w} -> 
    	MainPID ! {walk, self(), ["w"]},
	    humanloop(MainPID, GUIPID);
	{move,a} -> 
    	MainPID ! {walk, self(), ["a"]},
	    humanloop(MainPID, GUIPID);
	{move,s} -> 
    	MainPID ! {walk, self(), ["s"]},
	    humanloop(MainPID, GUIPID);
	{move,d} -> 
    	MainPID ! {walk, self(), ["d"]},
	    humanloop(MainPID, GUIPID);
	freeze ->
	    receive
		unfreeze ->
		    io:format("I'm unfrozen!"),
		    MainPID ! {unfreeze, self()},
		    humanloop(MainPID,GUIPID);
		kill ->
		    MainPID ! {unregister, self()},
		    io:format("I'm dead")
	    end;
	exit ->
	    io:format("Gunmanloop with pid ~p exited~n",[self()]);
	close ->
	    MainPID ! {walk, self(), exit}
    end.

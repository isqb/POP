-module(battle).
-export([sudden_impact/2]).

sudden_impact(PID1, PID2) ->
    receive 
	{player1shoot, Countdown} ->
	    if(Countdown == 0) ->
		    io:format("Player 1 shoots player 2, BANG!");
	      true ->
		    io:format("Player 1 is to eger to kill! Diskaaaad!!")
	    end;
	{player2shoot, Countdown} -> 
	    if(Countdown == 0) ->
		    io:format("Player 2 shoots player 1, BANG!");
	      true ->
		    io:format("Player 2 is to eger to kill! Diskaaaad!!")
	    end
    end.
			   
    

    

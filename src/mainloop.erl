-module(mainloop).
-export([mainloop/4]).

mainloop(UserPIDs, MapDict,GUIPID,FrozenDict) ->
    receive
	{register, PID,Coordinates} ->
	    io:format("Player ~p registered! ~n", [PID]),
	    MapDict2 = dict:store(Coordinates, PID, MapDict),
	    FrozenDict2 = dict:store(PID,false, FrozenDict),
	    mainloop([PID | UserPIDs], MapDict2,GUIPID,FrozenDict2);
	{unregister, PID} ->
	    MapDict2 = dict:erase(tools:getCoordinates(PID,MapDict),MapDict),
	    FrozenDict2 = dict:erase(PID,FrozenDict),
	    UserPIDs2 = lists:delete(PID,UserPIDs),
	    mainloop(UserPIDs2,MapDict2,GUIPID,FrozenDict2);
	{unfreeze, PID} ->
	    FrozenDict2 = dict:store(PID,false,FrozenDict),
	    PID ! {newposition, tools:getCoordinates(PID,MapDict)},
	    mainloop(UserPIDs,MapDict,GUIPID,FrozenDict2);
	{walk, GunmanPID, Direction} ->
	    {CoordinateX, CoordinateY} = tools:getCoordinates(GunmanPID, MapDict),
	    case Direction of
		["a"] ->
		    if CoordinateX<1 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs,MapDict,GUIPID,FrozenDict);	    
		       true -> %% walk(_, The old coordinates, The new coordinates,_m_,_,_)
			    tools:walk(MapDict,{CoordinateX,CoordinateY},{CoordinateX-1,CoordinateY},GunmanPID,GUIPID,FrozenDict,UserPIDs)
		    end;
		["d"] ->
		    if CoordinateX>99 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict, GUIPID, FrozenDict);
		       true ->
			    tools:walk(MapDict,{CoordinateX,CoordinateY},{CoordinateX+1,CoordinateY},GunmanPID,GUIPID,FrozenDict,UserPIDs)
		    end;
		["s"] ->
		    if CoordinateY>99 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict, GUIPID,FrozenDict);
		       true ->
			    tools:walk(MapDict,{CoordinateX,CoordinateY},{CoordinateX,CoordinateY+1},GunmanPID,GUIPID,FrozenDict,UserPIDs)
		    end;
		["w"] ->
		    if CoordinateY<1 ->
			    GunmanPID ! {newposition, {CoordinateX,CoordinateY}},
			    mainloop(UserPIDs, MapDict, GUIPID,FrozenDict);
		       true ->
			    tools:walk(MapDict,{CoordinateX,CoordinateY},{CoordinateX,CoordinateY-1},GunmanPID,GUIPID,FrozenDict,UserPIDs)
		    end
	    end;
	exit ->
	    tools:exitall(UserPIDs)
    end.

package com.example.base;

import java.util.List;

import com.example.model.DailyConsume;

public interface ISoapService {
	boolean syncDailyConsume(List<DailyConsume> _dailyConsumes, String _methodName);
}

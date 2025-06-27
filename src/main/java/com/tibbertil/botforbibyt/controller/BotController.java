package com.tibbertil.botforbibyt.controller;

import com.tibbertil.botforbibyt.dto.CoinDto;
import com.tibbertil.botforbibyt.dto.LogDto;
import com.tibbertil.botforbibyt.dto.OrderDto;
import com.tibbertil.botforbibyt.dto.StatusResponseDto;
import com.tibbertil.botforbibyt.dto.StrategyDto;
import com.tibbertil.botforbibyt.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BotController {

    private final BotService botService;

    //📄 GET /api/price/{symbol}
    //Получить последние N записей по монете.
    @GetMapping("/price/{symbol}")
    public List<CoinDto> coinRecords(@PathVariable String symbol){
        return botService.coinRecords(symbol);
    }

    //📄 GET /api/strategy
    //Получить список всех стратегий.
    @GetMapping("/strategy")
    public List<StrategyDto> listOfStrategies(){
        return botService.listOfStrategies();
    }

    //📄 POST /api/strategy
    //Создать стратегию.
    @PostMapping("/strategy")
    public StrategyDto createStrategy(@RequestBody StrategyDto strategyDto){
        return botService.createStrategy(strategyDto);
    }

    //📄 PUT /api/strategy/{id}
    //Обновить параметры стратегии (buyBelow, sellAbove и т.д.).
    @PutMapping("/strategy/{id}")
    public StrategyDto editStrategy(@PathVariable Long id, @RequestBody StrategyDto strategyDto) throws Exception {
        return botService.editStrategy(id, strategyDto);
    }

    //📄 DELETE /api/strategy/{id}
    // Удалить стратегию по id
    @DeleteMapping("/strategy/{id}")
    public StatusResponseDto deleteStrategy(@PathVariable Long id) {
        return botService.deleteStrategy(id);
    }

    //📄 GET /api/orders
    //Получить историю всех ордеров.
    @GetMapping("/orders")
    public List<OrderDto> orderHistory(){
        return botService.orderHistory();
    }

    //📄 GET /api/logs
    //Посмотреть логи (при необходимости для отладки).
    @GetMapping("/logs")
    public List<LogDto> listOfLogs(){
        return botService.listOfLogs();
    }
}

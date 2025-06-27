package com.tibbertil.botforbibyt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tibbertil.botforbibyt.Logger;
import com.tibbertil.botforbibyt.dto.BybitPriceResponseDto;
import com.tibbertil.botforbibyt.dto.CoinDto;
import com.tibbertil.botforbibyt.dto.LogDto;
import com.tibbertil.botforbibyt.dto.OrderDto;
import com.tibbertil.botforbibyt.dto.StatusResponseDto;
import com.tibbertil.botforbibyt.dto.StrategyDto;
import com.tibbertil.botforbibyt.entity.Level;
import com.tibbertil.botforbibyt.entity.LogEventEntity;
import com.tibbertil.botforbibyt.entity.OrderHistoryEntity;
import com.tibbertil.botforbibyt.entity.OrderType;
import com.tibbertil.botforbibyt.entity.PriceTickEntity;
import com.tibbertil.botforbibyt.entity.Status;
import com.tibbertil.botforbibyt.entity.StrategyConfigEntity;
import com.tibbertil.botforbibyt.exception.EntityNotFoundException;
import com.tibbertil.botforbibyt.repository.LogEventRepository;
import com.tibbertil.botforbibyt.repository.OrderHistoryRepository;
import com.tibbertil.botforbibyt.repository.PriceRickRepository;
import com.tibbertil.botforbibyt.repository.StrategyConfigRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BotService {

    private final LogEventRepository logEventRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final PriceRickRepository priceRickRepository;
    private final StrategyConfigRepository strategyConfigRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;

    // Проверка условия стратегий
    @Scheduled(fixedRate = 5000)
    public void scheduledStrategyCheck() {
        processStrategies();
    }

    @Scheduled(fixedRate = 10000)
    public void scheduledPriceUpdateFromBybit() {
        updateRealPrice("BTCUSDT"); // можно пройтись по всем уникальным символам стратегий
    }

    public BigDecimal fetchPriceFromBybit(String symbol) {
        try {
            String url = "https://api.bybit.com/v5/market/tickers?category=spot&symbol=" + symbol;
            RestTemplate restTemplate = new RestTemplate();
            String rawResponse = restTemplate.getForObject(url, String.class);
            logger.log(Level.INFO, "Bybit API Response: " + rawResponse, "BotService");

            // Пример обработки ответа для v5 (структура может отличаться!)
            JsonNode root = new ObjectMapper().readTree(rawResponse);
            JsonNode result = root.path("result").path("list").get(0);
            String lastPrice = result.path("lastPrice").asText();

            return new BigDecimal(lastPrice);
        } catch (Exception e) {
            logger.log(Level.ERROR, "Ошибка при получении цены с Bybit: " + e.getMessage(), "BotService");
        }
        return BigDecimal.ZERO;
    }


    public List<CoinDto> coinRecords(String symbol) {
        List<PriceTickEntity> priceTickEntityList = priceRickRepository
                .findAll((Sort.by(Sort.Direction.DESC, "timestamp")))
                .stream()
                .limit(50)
                .toList();

        return modelMapper.map(priceTickEntityList, new TypeToken<List<CoinDto>>() {}.getType());
        //Ты хочешь преобразовать список List<PriceTickEntity> в List<CoinDto>. Но Java работает с type erasure — то есть
        // она во время выполнения не знает, что это список CoinDto, а не просто List.
        //ModelMapper должен точно знать тип назначения, и тут на помощь приходит TypeToken.
    }

    public List<StrategyDto> listOfStrategies() {
        List<StrategyConfigEntity> strategyConfigEntityList = strategyConfigRepository.findAll();
        return modelMapper.map(strategyConfigEntityList, new TypeToken<List<StrategyDto>>() {}.getType());
    }

    @Transactional
    public StrategyDto createStrategy(StrategyDto strategyDto) {
        StrategyConfigEntity entity = modelMapper.map(strategyDto, StrategyConfigEntity.class);
        entity.setOrderAmount(entity.getBuyBelow());
        strategyConfigRepository.save(entity);
        return modelMapper.map(entity, StrategyDto.class);
    }

    @Transactional
    public StrategyDto editStrategy(Long id, StrategyDto strategyDto) throws Exception {
        StrategyConfigEntity entity = strategyConfigRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Strategy not found with id: " + id));

        // Обновляем только нужные поля
        if (strategyDto.getBuyBelow() != null) {
            entity.setBuyBelow(strategyDto.getBuyBelow());
        }
        if (strategyDto.getSellAbove() != null) {
            entity.setSellAbove(strategyDto.getSellAbove());
        }
        if (strategyDto.getOrderAmount() != null) {
            entity.setOrderAmount(strategyDto.getOrderAmount());
        }
        if (strategyDto.getSymbol() != null) {
            entity.setSymbol(strategyDto.getSymbol());
        }
        if (strategyDto.getActive() != null) {
            entity.setActive(strategyDto.getActive());
        }

        entity.setUpdatedAt(LocalDateTime.now());
        strategyConfigRepository.save(entity);

        return modelMapper.map(entity, StrategyDto.class);
    }

    public List<OrderDto> orderHistory() {
        List<OrderHistoryEntity> orderHistoryEntityList = orderHistoryRepository.findAll();
        return modelMapper.map(orderHistoryEntityList, new TypeToken<List<OrderDto>>() {}.getType());
    }

    public List<LogDto> listOfLogs() {
        List<LogEventEntity> logEventEntities = logEventRepository.findAll();
        return modelMapper.map(logEventEntities, new TypeToken<List<LogDto>>() {}.getType());
    }

    public void updateRealPrice(String symbol) {
        BigDecimal price = fetchPriceFromBybit(symbol);
        if (price.compareTo(BigDecimal.ZERO) > 0) {
            updatePrice(symbol, price); // твой уже готовый метод
        }
    }

    // Метод обновления цены монеты
    public void updatePrice(String symbol, BigDecimal price) {
        PriceTickEntity entity = new PriceTickEntity();
        entity.setSymbol(symbol);
        entity.setPrice(price);
        entity.setTimestamp(LocalDateTime.now());
        priceRickRepository.save(entity);
    }

    // Метод для обработки активных стратегий
    public void processStrategies() {
        List<StrategyConfigEntity> strategies = strategyConfigRepository.findByActiveTrue();

        for (StrategyConfigEntity strategy : strategies) {
            PriceTickEntity lastPrice = priceRickRepository
                    .findTop1BySymbolOrderByTimestampDesc(strategy.getSymbol());

            if (lastPrice == null) continue;

            BigDecimal currentPrice = lastPrice.getPrice();

            if (currentPrice.compareTo(strategy.getBuyBelow()) < 0) {
                createOrder(strategy, OrderType.BUY, currentPrice);
            } else if (currentPrice.compareTo(strategy.getSellAbove()) > 0) {
                createOrder(strategy, OrderType.SELL, currentPrice);
            }
        }
    }

    private void createOrder(StrategyConfigEntity strategy, OrderType type, BigDecimal price) {
        OrderHistoryEntity order = new OrderHistoryEntity();
        order.setSymbol(strategy.getSymbol());
        order.setOrderType(type);
        order.setPrice(price);
        order.setQuantity(strategy.getOrderAmount());
        order.setStatus(Status.EXECUTED); // можно позже добавить обработку исполнения
        order.setCreatedAt(LocalDateTime.now());
        order.setExecutedAt(LocalDateTime.now());
        order.setOrderId(UUID.randomUUID());

        orderHistoryRepository.save(order);

        logger.log(Level.INFO, "Order executed: " + order.getOrderType() + " " + order.getSymbol() + " at " + price, "BotService");
    }

    public StatusResponseDto deleteStrategy(Long id) {
        StrategyConfigEntity entity = strategyConfigRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Strategy not found with id: " + id));

        strategyConfigRepository.delete(entity);

        return StatusResponseDto.ok();
    }
}

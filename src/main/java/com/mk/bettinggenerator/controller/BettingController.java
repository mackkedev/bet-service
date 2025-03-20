package com.mk.bettinggenerator.controller;


import com.mk.bettinggenerator.dto.*;
import com.mk.bettinggenerator.service.BettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1/predictions")
@RequiredArgsConstructor
public class BettingController {

    private final BettingService bettingService;

    @GetMapping
    public ResponseEntity<BettingResponse> getBettingRows(@RequestParam int matches, @RequestParam int gardering) {
        return new ResponseEntity<>(new BettingResponse(bettingService.generateBets(matches, gardering)), HttpStatus.OK);
    }

    @GetMapping("/multiple")
    public ResponseEntity<BettingListResponse> getBettingCoupons(@RequestBody PixCouponRequest pixCouponRequest) {
        return new ResponseEntity<>(new BettingListResponse(bettingService.generateMultipleCoupons(pixCouponRequest)), HttpStatus.OK);
    }

    @GetMapping("/multiples")
    public ResponseEntity<MultiplePixResponse> getBettingCoupons(@RequestBody PixRandomCouponRequest pixCouponRequest) {
        return new ResponseEntity<>(new MultiplePixResponse(bettingService.generateMultipleCoupons(pixCouponRequest)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MultiplePixResponse> createBettingCoupon(@RequestBody CouponRequest couponRequest) {
        return new ResponseEntity<>(new MultiplePixResponse(bettingService.createBettingCoupons(couponRequest)), HttpStatus.CREATED);
    }

    @GetMapping("/hedge")
    public ResponseEntity<PixPredictionResponse> getGeneratedPixCoupon(@RequestBody MultiplePixRequest request) {
        return new ResponseEntity<>(new PixPredictionResponse(bettingService.generateMultipleCustomPixCoupons(request)), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BetResponse> createBettingTicket(@RequestBody BetPredictionRequest betPredictionRequest) {
        return new ResponseEntity<>(new BetResponse(bettingService.createBettingCoupon(betPredictionRequest)), HttpStatus.CREATED);
    }

    @GetMapping("/fullhit")
    public ResponseEntity<FullHitGameResponse> getGeneratedFullHit(@RequestBody FullHitPredictionRequest fullHitPrediction) {
        return new ResponseEntity<>(new FullHitGameResponse(bettingService.createFullHitPrediction(fullHitPrediction)), HttpStatus.OK);
    }
}

package com.enfec.demo.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Component
public class SeatCategory {
	public int Category_ID;
	public String Category_Name;
	public double Price;
}

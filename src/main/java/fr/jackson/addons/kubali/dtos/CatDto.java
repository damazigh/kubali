package fr.jackson.addons.kubali.dtos;

import com.fasterxml.jackson.annotation.JsonFilter;

import fr.jackson.addons.kubali.annotations.ConditionalSerialization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConditionalSerialization
@JsonFilter("CatDtoFilter")
public class CatDto {
	private String name;
	private Integer age;
	private String color;
}

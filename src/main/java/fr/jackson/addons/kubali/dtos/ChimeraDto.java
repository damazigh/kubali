package fr.jackson.addons.kubali.dtos;

import fr.jackson.addons.kubali.annotations.ConditionalSerialization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConditionalSerialization
public class ChimeraDto {
	private GoatDto halfGoat;
	private LionDto halfLion;

	public ChimeraDto() {
		halfGoat = new GoatDto();
		halfLion = new LionDto();
	}
}

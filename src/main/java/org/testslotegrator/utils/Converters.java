package org.testslotegrator.utils;

import org.testslotegrator.model.PlayerRequestDTO;
import org.testslotegrator.model.PlayerResponseDTO;

public class Converters {

	// если бы не падали из-зв схемы, я бы преобразовывал и проверял так
	public static PlayerResponseDTO toExpectedPlayerResponse(PlayerRequestDTO req, Integer id) {
		if (req == null) return null;
		return PlayerResponseDTO.builder()
				.id(id)
				.username(req.getUsername())
				.email(req.getEmail())
				.name(req.getName())
				.surname(req.getSurname())
				.build();
	}

	public static PlayerResponseDTO toExpectedPlayerResponse(PlayerRequestDTO req) {
		return toExpectedPlayerResponse(req, null);
	}

}

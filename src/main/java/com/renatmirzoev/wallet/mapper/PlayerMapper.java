package com.renatmirzoev.wallet.mapper;

import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.rest.model.CreatePlayerRequest;
import com.renatmirzoev.wallet.rest.model.GetPlayerResponse;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PlayerMapper {

    Player fromCreateRequest(CreatePlayerRequest request);

    GetPlayerResponse toGetPlayerResponse(Player player);
}

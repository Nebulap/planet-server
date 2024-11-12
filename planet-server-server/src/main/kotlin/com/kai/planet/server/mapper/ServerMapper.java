package com.kai.planet.server.mapper;

import com.kai.planet.common.domain.entity.server.Server;
import com.kai.planet.common.domain.request.server.AddServerRequest;
import org.mapstruct.Mapper;

/**
 *
 * @since 2024/11/12 10:59
 * @author 29002
 * @version 1.0.0
 */


@Mapper(componentModel = "spring")
public interface ServerMapper {
    Server toEntity(AddServerRequest request);
}

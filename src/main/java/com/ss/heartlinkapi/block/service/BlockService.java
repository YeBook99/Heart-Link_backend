package com.ss.heartlinkapi.block.service;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.block.repository.BlockRepository;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;

@Service
public class BlockService {

	private final BlockRepository blockRepository;

	public BlockService(BlockRepository blockRepository) {
		this.blockRepository = blockRepository;
	}

	public boolean isUserBlockedByCouple(UserEntity blocker, CoupleEntity couple) {
		return blockRepository.existsByBlockerIdAndCoupleId(blocker, couple);
	}

}

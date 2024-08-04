package com.bayu.regulatory.service.impl;

import com.bayu.regulatory.repository.RegulatoryDataChangeRepository;
import com.bayu.regulatory.service.RegulatoryDataChangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegulatoryDataChangeServiceImpl implements RegulatoryDataChangeService {

    private final RegulatoryDataChangeRepository regulatoryDataChangeRepository;

}

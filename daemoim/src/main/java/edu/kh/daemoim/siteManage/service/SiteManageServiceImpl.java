package edu.kh.daemoim.siteManage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.siteManage.mapper.SiteManageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SiteManageServiceImpl implements SiteManageService {

	private SiteManageMapper mapper;
}

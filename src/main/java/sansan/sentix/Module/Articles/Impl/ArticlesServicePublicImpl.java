package sansan.sentix.Module.Articles.Impl;

import org.springframework.stereotype.Service;
import sansan.sentix.Module.Articles.Service.ArticlesRawService;
import sansan.sentix.Module.Articles.Service.ArticlesServicePublic;

import javax.annotation.Resource;

@Service
public class ArticlesServicePublicImpl implements ArticlesServicePublic {

    @Resource
    private ArticlesRawService articlesRawService;

    @Override
    public void analyzeNewsRaw(Long idArticlesRaw) {
        articlesRawService.analyzeNewsRaw(idArticlesRaw);
    }
}

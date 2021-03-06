package com.BibleQuote.dal;

import android.content.Context;

import com.BibleQuote.controllers.CacheModuleController;
import com.BibleQuote.dal.repository.IBookRepository;
import com.BibleQuote.dal.repository.IChapterRepository;
import com.BibleQuote.dal.repository.IModuleRepository;

public interface ILibraryUnitOfWork<TModuleId, TModule, TBook> {
	
	public IModuleRepository<TModuleId, TModule> getModuleRepository();
	
	public IBookRepository<TModule, TBook> getBookRepository();
	
	public IChapterRepository<TBook> getChapterRepository();
	
	public Context getLibraryContext();
	
	public CacheModuleController<TModule> getCacheModuleController();
	
}

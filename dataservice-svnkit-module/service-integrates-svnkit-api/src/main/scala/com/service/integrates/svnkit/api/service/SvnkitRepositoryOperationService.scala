package com.service.integrates.svnkit.api.service

import java.io.File
import java.util

import com.service.integrates.svnkit.api.ServiceSvnkitConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.tmatesoft.svn.core.{SVNException, SVNURL}
import org.tmatesoft.svn.core.io.{ISVNSession, SVNRepository, SVNRepositoryFactory}

import scala.collection.JavaConversions._

trait SvnkitRepositoryOperationService {

  @Autowired
  val groupService: SvnkitGroupOperationService = null

  @Autowired
  val property: ServiceSvnkitConfigurationProperties = null

  @throws[SVNException]
  def create(url: SVNURL): SVNRepository = {
    SVNRepositoryFactory.create(url)
  }

  @throws[SVNException]
  def create(url: SVNURL, options: ISVNSession): SVNRepository = {
    SVNRepositoryFactory.create(url, options)
  }

  //  @throws[SVNException]
  //  def createLocalRepository(path: File, uuid: String, enableRevisionProperties: Boolean, force: Boolean): SVNURL = {
  //    SVNRepositoryFactory.createLocalRepository(path, uuid, enableRevisionProperties, force)
  //  }
  //
  //  @throws[SVNException]
  //  def createLocalRepository(path: File, uuid: String, enableRevisionProperties: Boolean, force: Boolean, pre14Compatible: Boolean): SVNURL = {
  //    SVNRepositoryFactory.createLocalRepository(path, uuid, enableRevisionProperties, force, pre14Compatible)
  //  }
  //
  //  @throws[SVNException]
  //  def createLocalRepository(path: File, uuid: String, enableRevisionProperties: Boolean, force: Boolean, pre14Compatible: Boolean, pre15Compatible: Boolean): SVNURL = {
  //    SVNRepositoryFactory.createLocalRepository(path, uuid, enableRevisionProperties, force, pre14Compatible, pre15Compatible)
  //  }
  //
  //  @throws[SVNException]
  //  def createLocalRepository(path: File, uuid: String, enableRevisionProperties: Boolean, force: Boolean, pre14Compatible: Boolean, pre15Compatible: Boolean, pre16Compatible: Boolean): SVNURL = {
  //    SVNRepositoryFactory.createLocalRepository(path, uuid, enableRevisionProperties, force, pre14Compatible, pre15Compatible, pre16Compatible)
  //  }
  //
  //  @throws[SVNException]
  //  def createLocalRepository(path: File, uuid: String, enableRevisionProperties: Boolean, force: Boolean, pre14Compatible: Boolean, pre15Compatible: Boolean, pre16Compatible: Boolean, pre17Compatible: Boolean, with17Compatible: Boolean): SVNURL = {
  //    SVNRepositoryFactory.createLocalRepository(path, uuid, enableRevisionProperties, force, pre14Compatible, pre15Compatible, pre16Compatible, pre17Compatible, with17Compatible)
  //  }

  /**
    * ???????????????????????????
    *
    * @param path
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    * @return
    */
  @throws[SVNException]
  def createLocalRepository(path: File, enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): SVNURL = {
    // ????????????
    val url = createLocalRepository(path, enableRevisionProperties, force)
    // ????????????
    addRights(path.getName, "/", values)
    url
  }

  /**
    * ????????????????????????
    *
    * @param path
    * @param enableRevisionProperties
    * @param force
    * @throws
    */
  @Async(value = "ServiceExecutor")
  @throws[SVNException]
  def asyncCreateLocalRepository(path: File, enableRevisionProperties: Boolean, force: Boolean): Unit = {
    try {
      // ????????????
      createLocalRepository(path, enableRevisionProperties, force)
    } catch {
      case _: Throwable => /* ignore */
    }
  }

  /**
    * ?????????????????????????????????
    *
    * @param path
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    */
  @Async(value = "ServiceExecutor")
  @throws[SVNException]
  def asyncCreateLocalRepository(path: File, enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): Unit = {
    try {
      // ????????????
      createLocalRepository(path, enableRevisionProperties, force, values)
    } catch {
      case _: Throwable => /* ignore */
    }
  }

  /**
    * ??????????????????
    *
    * @param repositoryName
    * @param enableRevisionProperties
    * @param force
    * @throws
    */
  @throws[SVNException]
  def createLocalRepository(repositoryName: String, enableRevisionProperties: Boolean, force: Boolean): Unit = {
    createLocalRepository(new File(property.getPath, repositoryName), enableRevisionProperties, force)
  }

  /**
    * ???????????????????????????
    *
    * @param repositoryName
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    */
  @throws[SVNException]
  def createLocalRepository(repositoryName: String, enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): Unit = {
    createLocalRepository(new File(property.getPath, repositoryName), enableRevisionProperties, force, values)
  }

  /**
    * ????????????????????????
    *
    * @param repositoryNames
    * @param enableRevisionProperties
    * @param force
    * @throws
    */
  @throws[SVNException]
  def createLocalRepositoryBatch(repositoryNames: util.List[String], enableRevisionProperties: Boolean, force: Boolean): Unit = {
    // ??????
    repositoryNames.par.foreach(repositoryName => {
      // ??????
      asyncCreateLocalRepository(new File(property.getPath, repositoryName), enableRevisionProperties, force)
    })
  }

  /**
    * ?????????????????????????????????
    *
    * @param repositoryNames
    * @param enableRevisionProperties
    * @param force
    * @param values
    * @throws
    */
  @throws[SVNException]
  def createLocalRepositoryBatch(repositoryNames: util.List[String], enableRevisionProperties: Boolean, force: Boolean, values: util.Map[String, String]): Unit = {
    // ??????
    repositoryNames.par.foreach(repositoryName => {
      // ??????
      asyncCreateLocalRepository(new File(property.getPath, repositoryName), enableRevisionProperties, force, values)
    })
  }

  /**
    * ??????????????????
    *
    * @param path
    * @param enableRevisionProperties
    * @param force
    * @throws
    * @return
    */
  @throws[SVNException]
  def createLocalRepository(path: File, enableRevisionProperties: Boolean, force: Boolean): SVNURL

  /**
    * ??????????????????
    *
    * @param repositoryName ????????????
    * @param repositoryPath ????????????
    * @return
    */
  @throws[Exception]
  def listRights(repositoryName: String, repositoryPath: String): util.Map[String, util.Map[String, String]]

  /**
    * ??????????????????????????????
    *
    * @param repositoryName ????????????
    * @return Map(?????? -> ??????)
    */
  @throws[Exception]
  def listRightUsers(repositoryName: String): util.Map[String, String]

  /**
    * ??????????????????
    *
    * @param repositoryName ????????????
    * @param repositoryPath ????????????
    * @param values         ?????????
    */
  @throws[Exception]
  def addRights(repositoryName: String, repositoryPath: String, values: util.Map[String, String]): Unit

  /**
    * ??????????????????
    *
    * @param repositoryName ????????????
    * @param repositoryPath ????????????
    * @param names          ????????????
    * @return
    */
  @throws[Exception]
  def deleteRights(repositoryName: String, repositoryPath: String, names: Array[String]): Unit

  /**
    * ??????????????????
    *
    * @return
    */
  @throws[Exception]
  def listRepositories(): util.List[String]

  /**
    * ??????????????????????????????
    *
    * @param username ????????????
    * @return
    */
  @throws[Exception]
  def userHaveRightRepositories(username: String): util.List[String]

  /**
    * ??????????????????????????????
    *
    * @param groupName ????????????
    * @return
    */
  @throws[Exception]
  def groupHaveRightRepositories(groupName: String): util.List[String]
}

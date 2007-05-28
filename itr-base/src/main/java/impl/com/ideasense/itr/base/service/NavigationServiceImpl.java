/* $Id$ */
/*
 ******************************************************************************
 *   Copyright (C) 2007 IDEASense, (hasin & hasan) 
 *
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 ******************************************************************************
 * $LastChangedBy$
 * $LastChangedDate$
 * $LastChangedRevision$
 ******************************************************************************
*/
package impl.com.ideasense.itr.base.service;

import com.ideasense.itr.base.service.NavigationService;
import com.ideasense.itr.base.service.ConfigurationService;
import com.ideasense.itr.base.navigation.ServiceNavigationTree;
import com.ideasense.itr.base.navigation.Company;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Implementation of {@code NavigationService}.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class NavigationServiceImpl implements NavigationService {

  private final Logger LOG = LogManager.getLogger(getClass());
  private final ConfigurationService mConfigurationService;
  private final List<Company> mCompanies;
  private final Map<String, Company> mCacheCompanyMap =
      new HashMap<String, Company>();


  public NavigationServiceImpl(final ConfigurationService pConfigurationService)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Constructing NavigationService - " + pConfigurationService);
    }
    if (pConfigurationService == null) {
      throw new IllegalArgumentException("Configuration service instance " +
          "is returning null. it must be not NULL.");
    } else {
      mConfigurationService = pConfigurationService;
      mCompanies = mConfigurationService.getITRMapping().getCompanies();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Found list of companies - " + mCompanies);
      }
    }
  }

  /**
   * {@inheritDoc}
   * @param pCompanyName {@inheritDoc}
   * @return {@inheritDoc}
   */
  public ServiceNavigationTree getServiceTree(final String pCompanyName) {
    if (pCompanyName == null || pCompanyName.length() == 0) {
      throw new IllegalArgumentException("Company name is expecting not null.");
    }
    // Look up in cache table.
    Company company = mCacheCompanyMap.get(pCompanyName);
    if (company == null) {
      // Find through interating all companies
      company = findFromList(pCompanyName);
      // Update cache map
      mCacheCompanyMap.put(pCompanyName, company);

      // Still company is null
      if (company != null) {
        return company.getServiceNavigationTree();
      }
    } else {
      return company.getServiceNavigationTree();
    }
    return null;
  }

  private Company findFromList(final String pCompanyName) {
    LOG.debug("Looking up company name - " + pCompanyName);
    for (final Company company : mCompanies) {
      if (pCompanyName.toLowerCase().equals(company.getName().toLowerCase())) {
        return company;
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   * @return {@inheritDoc}
   */
  public List<ServiceNavigationTree> getAllServiceTrees() {
    List<ServiceNavigationTree> treeList =
        new ArrayList<ServiceNavigationTree>();
    for (final Company company : mCompanies) {
      treeList.add(company.getServiceNavigationTree());
    }
    return treeList;
  }
}

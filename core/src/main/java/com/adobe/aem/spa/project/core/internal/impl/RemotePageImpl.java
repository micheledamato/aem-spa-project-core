/*
 * Copyright 2020 Adobe. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.adobe.aem.spa.project.core.internal.impl;

import com.adobe.aem.spa.project.core.models.RemotePage;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * RemotePage model implementation - Page that allows rendering and editing in AEM of a
 * remote SPA that exists at the URL defined in the page properties
 */
@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = { RemotePage.class, ContainerExporter.class },
    resourceType = { RemotePageImpl.RESOURCE_TYPE_SPA, RemotePageImpl.RESOURCE_TYPE_NEXT },
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class RemotePageImpl extends PageImpl implements RemotePage {

    static final String RESOURCE_TYPE_SPA = "spa-project-core/components/remotepage";
    static final String RESOURCE_TYPE_NEXT = "eurobet-site/components/eurobet-remotepagenext";

    @Self
    private SlingHttpServletRequest request;

     @ValueMapValue
     private String remoteSPAUrl;

     @Override
     public String getRemoteSPAUrl() {
        String spaUrl = "";

        if(remoteSPAUrl != null) {
            spaUrl = remoteSPAUrl;
        } else {
            PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);
            Page page = pageManager.getContainingPage(request.getResource());
            if(page != null) {
                InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(page.getContentResource());
                spaUrl = pageProperties.getInherited("remoteSPAUrl", "");
            }
        }
        return spaUrl;
    }
}

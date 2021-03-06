/**
 * Java Class : IPropertyContent.java
 *
 * Description :
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing,
 *    software distributed under the License is distributed on an
 *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *    KIND, either express or implied.  See the License for the
 *    specific language governing permissions and limitations
 *    under the License.
 *
 * @category   Util
 * @package    com.modeliosoft.modelio.sysml.utils
 *   @author     Modelio
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    2.0.08
 **/
package org.modelio.module.attacktreedesigner.property;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

/**
 * This interface defines the contract of all property pages
 * @author ebrosse
 */
@objid ("21b5d5bc-3ad4-477b-ac33-db6a2af865ee")
public interface IPropertyContent {
    /**
     * This method handles the changes of the given property, identified by its row index, of a selected element
     * to a new value.
     * @param MObject : the selected element
     * @param row : the row of the changed property
     * @param value : the new value of the property
     */
    @objid ("415c6d0a-6107-4ee7-a28c-9a89c1b073fa")
    void changeProperty(ModelElement element, int row, String value);

    /**
     * This method handles the construction of the property table of a selected element
     * @param MObject : the selected element
     * @param table : the property table to fulfill
     */
    @objid ("336cdd6a-bf2d-4551-9ae6-1eae039c23df")
    void update(ModelElement element, IModulePropertyTable table);

}

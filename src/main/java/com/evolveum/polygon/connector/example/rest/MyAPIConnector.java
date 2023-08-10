/**
 * Copyright (c) 2016 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.polygon.connector.myapi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import org.identityconnectors.common.Base64;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.*;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.TestOp;

import com.evolveum.polygon.rest.AbstractRestConnector;

import org.identityconnectors.framework.spi.PoolableConnector;
import org.identityconnectors.framework.spi.operations.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



@ConnectorClass(displayNameKey = "myapi.rest.display", configurationClass = MyAPIConfiguration.class)
public class MyAPIConnector extends AbstractRestConnector<MyAPIConfiguration> implements TestOp, SchemaOp {

	 // Déclaration des constantes pour les attributs
	public static final String ATTR_ID = "id";
	public static final String ATTR_USERNAME = "username";
	public static final String ATTR_EMAIL = "email";
	public static final String ATTR_FIRST_NAME = "first_name";
	public static final String ATTR_LAST_NAME = "last_name";
	public static final String ATTR_BIRTHDATE = "birthdate";
	public static final String ATTR_ADDRESS = "address";
	public static final String ATTR_PHONE = "phone";
	public static final String ATTR_EMPLOYEE_TYPE = "employee_type";
	public static final String ATTR_UPN = "upn";
	public static final String ATTR_MANAGER_ID = "manager_id";
	public static final String ATTR_AZURE_ID = "azure_id";
	public static final String ATTR_FULL_NAME = "full_name";
	public static final String ATTR_EMPLOYEE_NUMBER = "employee_number";

	@Override
	public void test() {
		String apiUrl = getConfiguration().getBaseUrl() + "/users"; // Assurez-vous que c'est le bon endpoint pour tester la connexion.
		HttpGet request = new HttpGet(apiUrl);
		HttpResponse response = execute(request);
		processResponseErrors(response);
	}

	@Override
	public Schema schema() {
		SchemaBuilder schemaBuilder = new SchemaBuilder(MyAPIConnector.class);
	        buildUserObjectClass(schemaBuilder);
	        return schemaBuilder.build();
	}

	private void buildUserObjectClass(SchemaBuilder schemaBuilder) {
	        ObjectClassInfoBuilder objClassBuilder = new ObjectClassInfoBuilder();
		
		/**
  		* Chaque attribut est défini en utilisant un AttributeInfoBuilder qui permet de
  		* définir les propriétés de l'attribut (par exemple, s'il est obligatoire, s'il est multivalué, etc.).
    		* Une fois que tous les attributs ont été définis, ils sont ajoutés à la classe d'objet User
      		* en utilisant objClassBuilder.addAttributeInfo(...). Enfin, la classe d'objet est ajoutée
		* au schéma global du connecteur avec schemaBuilder.defineObjectClass(...)
  		*/
	
	        // ID & USERNAME sont les attributs par défaut
	
	        AttributeInfoBuilder attrIdBuilder = new AttributeInfoBuilder(ATTR_ID);
	        attrIdBuilder.setRequired(true);
	        objClassBuilder.addAttributeInfo(attrIdBuilder.build());
	
	        AttributeInfoBuilder attrUsernameBuilder = new AttributeInfoBuilder(ATTR_USERNAME);
	        attrUsernameBuilder.setRequired(true);
	        objClassBuilder.addAttributeInfo(attrUsernameBuilder.build());
	
	        AttributeInfoBuilder attrEmailBuilder = new AttributeInfoBuilder(ATTR_EMAIL);
	        objClassBuilder.addAttributeInfo(attrEmailBuilder.build());
	
	        AttributeInfoBuilder attrFirstNameBuilder = new AttributeInfoBuilder(ATTR_FIRST_NAME);
	        attrFirstNameBuilder.setRequired(true);
	        objClassBuilder.addAttributeInfo(attrFirstNameBuilder.build());
	
	        AttributeInfoBuilder attrLastNameBuilder = new AttributeInfoBuilder(ATTR_LAST_NAME);
	        attrLastNameBuilder.setRequired(true);
	        objClassBuilder.addAttributeInfo(attrLastNameBuilder.build());

		AttributeInfoBuilder attrBirthdateBuilder = new AttributeInfoBuilder(ATTR_BIRTHDATE);
	        objClassBuilder.addAttributeInfo(attrBirthdateBuilder.build());
	
	        AttributeInfoBuilder attrAddressBuilder = new AttributeInfoBuilder(ATTR_ADDRESS);
	        objClassBuilder.addAttributeInfo(attrAddressBuilder.build());
	
	        AttributeInfoBuilder attrPhoneBuilder = new AttributeInfoBuilder(ATTR_PHONE);
	        objClassBuilder.addAttributeInfo(attrPhoneBuilder.build());
	
	        AttributeInfoBuilder attrEmployeeTypeBuilder = new AttributeInfoBuilder(ATTR_EMPLOYEE_TYPE);
	        attrEmployeeTypeBuilder.setRequired(true);
	        objClassBuilder.addAttributeInfo(attrEmployeeTypeBuilder.build());
	
	        AttributeInfoBuilder attrUpnBuilder = new AttributeInfoBuilder(ATTR_UPN);
	        objClassBuilder.addAttributeInfo(attrUpnBuilder.build());
	
	        AttributeInfoBuilder attrManagerIdBuilder = new AttributeInfoBuilder(ATTR_MANAGER_ID);
	        attrManagerIdBuilder.setRequired(true);
	        objClassBuilder.addAttributeInfo(attrManagerIdBuilder.build());
	
	        AttributeInfoBuilder attrAzureIdBuilder = new AttributeInfoBuilder(ATTR_AZURE_ID);
	        objClassBuilder.addAttributeInfo(attrAzureIdBuilder.build());
	
	        AttributeInfoBuilder attrFullNameBuilder = new AttributeInfoBuilder(ATTR_FULL_NAME);
	        objClassBuilder.addAttributeInfo(attrFullNameBuilder.build());
	
	        AttributeInfoBuilder attrEmployeeNumberBuilder = new AttributeInfoBuilder(ATTR_EMPLOYEE_NUMBER);
	        attrEmployeeNumberBuilder.setRequired(true);
	        objClassBuilder.addAttributeInfo(attrEmployeeNumberBuilder.build());

		schemaBuilder.defineObjectClass(objClassBuilder.build());

	}

	

}

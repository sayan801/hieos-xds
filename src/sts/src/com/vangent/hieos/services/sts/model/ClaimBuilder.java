/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2011 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vangent.hieos.services.sts.model;

import com.vangent.hieos.policyutil.util.AttributeConfig;
import com.vangent.hieos.policyutil.util.AttributeConfig.AttributeIdType;
import com.vangent.hieos.policyutil.util.PolicyConfig;
import com.vangent.hieos.policyutil.util.PolicyConstants;
import com.vangent.hieos.services.sts.exception.STSException;
import com.vangent.hieos.services.sts.util.STSUtil;
import com.vangent.hieos.xutil.exception.XPathHelperException;
import com.vangent.hieos.xutil.xml.XPathHelper;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

/**
 *
 * @author Bernie Thuman
 */
public class ClaimBuilder {

    private final static Logger logger = Logger.getLogger(ClaimBuilder.class);

    /**
     *
     * @param requestData
     * @return
     * @throws STSException
     */
    public List<Claim> parse(STSRequestData requestData) throws STSException {
        OMElement claimsNode = requestData.getClaimsNode();
        List<Claim> claims = new ArrayList<Claim>();
        this.parse(claimsNode, claims);
        this.validate(claims);
        return claims;
    }

    /**
     *
     * @param claimsNode
     * @param claims
     */
    private void parse(OMElement claimsNode, List<Claim> claims) throws STSException {
        try {
            // Get all ClaimType nodes.
            List<OMElement> claimTypeNodes = XPathHelper.selectNodes(claimsNode, "./ns:ClaimType", PolicyConstants.XSPA_CLAIMS_NS);
            for (OMElement claimTypeNode : claimTypeNodes) {
                Claim claim = this.getClaim(claimTypeNode);
                if (claim != null) {
                    claims.add(claim);
                }
            }
        } catch (XPathHelperException ex) {
            throw new STSException(ex.getMessage());  // Rethrow.
        }
    }

    /**
     *
     * @param claims
     * @throws STSException
     */
    private void validate(List<Claim> claims) throws STSException {
        // Validate proper attributes are available.
        PolicyConfig pConfig = STSUtil.getPolicyConfig();
        List<AttributeConfig> claimAttributeConfigs = pConfig.getAttributeConfigs(AttributeConfig.AttributeIdType.CLAIM_ID);
        for (AttributeConfig claimAttributeConfig : claimAttributeConfigs) {
            String nameToValidate = claimAttributeConfig.getId();
            boolean foundName = false;
            for (Claim claim : claims) {
                if (claim.getName().equalsIgnoreCase(nameToValidate)) {
                    foundName = true;
                    break;
                }
            }
            if (foundName == false) {
                //logger.warn("Missing " + nameToValidate + " attribute");
                throw new STSException("Missing " + nameToValidate + " attribute");
            }
        }
    }

    /**
     * 
     * @param claimTypeNode
     * @return
     * @throws STSException
     */
    private Claim getClaim(OMElement claimTypeNode) throws STSException {
        String claimTypeURI = claimTypeNode.getAttributeValue(new QName("Uri"));

        // Found ClaimType ... now, get its ClaimValue.
        OMElement xspaClaimValueNode = claimTypeNode.getFirstChildWithName(new QName(PolicyConstants.XSPA_CLAIMS_NS, "ClaimValue"));
        if (xspaClaimValueNode == null) {
            // FIXME: PUT DEBUG/DEFAULT?
            return null; // Get out.
        }
        PolicyConfig pConfig = STSUtil.getPolicyConfig();
        AttributeConfig claimConfig = pConfig.getAttributeConfig(AttributeIdType.CLAIM_ID, claimTypeURI);
        Claim claim;
        switch (claimConfig.getAttributeType()) {
            case ANY:
                AnyValueClaim anyValueClaim = new AnyValueClaim();
                anyValueClaim.setName(claimTypeURI);
                OMElement claimContentNode = xspaClaimValueNode.getFirstElement();
                if (claimContentNode != null) {
                    anyValueClaim.setContentNode(claimContentNode);
                    // Now, parse it.
                    //codedValueClaim.setNodeName(claimContentNode.getLocalName());
                    //CodedValue codedValue = codedValueClaim.getCodedValue();
                    //String code = claimContentNode.getAttributeValue(new QName("code"));
                    //String codeSystem = claimContentNode.getAttributeValue(new QName("codeSystem"));
                    //String codeSystemName = claimContentNode.getAttributeValue(new QName("codeSystemName"));
                    //String displayName = claimContentNode.getAttributeValue(new QName("displayName"));
                    //codedValue.setCode(code);
                    //codedValue.setCodeSystem(codeSystem);
                    //codedValue.setCodeSystemName(codeSystemName);
                    //codedValue.setDisplayName(displayName);
                } else {
                    throw new STSException("Any element type expected for Claim URI = " + claimTypeURI);
                }
                claim = anyValueClaim;
                break;
            case STRING:
            default:  // fall through
                // Get the String value.
                String valueText = xspaClaimValueNode.getText();
                SimpleStringClaim simpleStringClaim = new SimpleStringClaim();
                simpleStringClaim.setName(claimTypeURI);
                simpleStringClaim.setValue(valueText);
                claim = simpleStringClaim;
        }
        return claim;
    }
}

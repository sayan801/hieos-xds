/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2010 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vangent.hieos.hl7v3util.model.subject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Bernie Thuman
 */
public class Subject implements Cloneable {

    private List<SubjectIdentifier> subjectIdentifiers = new ArrayList<SubjectIdentifier>();
    private List<SubjectIdentifier> subjectOtherIdentifiers = new ArrayList<SubjectIdentifier>();
    private List<SubjectName> subjectNames = new ArrayList<SubjectName>();
    private List<Address> addresses = new ArrayList<Address>();
    private List<TelecomAddress> telecomAddresses = new ArrayList<TelecomAddress>();
    private List<SubjectPersonalRelationship> subjectPersonalRelationships = new ArrayList<SubjectPersonalRelationship>();
    private CodedValue gender = null;
    private CodedValue maritalStatus = null;
    private CodedValue religiousAffiliation = null;
    private CodedValue race = null;
    private CodedValue ethnicGroup = null;
    private Custodian custodian = null;
    private Date birthTime = null;
    private Boolean deceasedIndicator = null;
    private Date deceasedTime = null;
    private Boolean multipleBirthIndicator = null;
    private Integer multipleBirthOrderNumber = null;
    private int matchConfidencePercentage = 0;
    private String internalId = null;
    private SubjectType type = SubjectType.ENTERPRISE; // Default.

    /**
     *
     */
    public enum SubjectType {

        /**
         *
         */
        ENTERPRISE,
        /**
         * 
         */
        SYSTEM,
        /**
         * 
         */
        PERSONAL_RELATIONSHIP,
        /**
         *
         */
        VOIDED
    };

    /**
     *
     * @param subjectType
     * @return
     */
    public static String getSubjectTypeValue(Subject.SubjectType subjectType) {
        String value = "";
        switch (subjectType) {
            case ENTERPRISE:
                value = "E";
                break;
            case SYSTEM:
                value = "S";
                break;
            case PERSONAL_RELATIONSHIP:
                value = "P";
                break;
            default:
                value = "V";
                break;
        }
        return value;
    }

    /**
     * NOTE: Could have built a full enumeration, but decided to be overkill.
     *
     * @param type
     * @return
     */
    public static Subject.SubjectType getSubjectType(String type) {
        Subject.SubjectType subjectType = SubjectType.ENTERPRISE;
        if (type.equalsIgnoreCase("E")) {
            subjectType = SubjectType.ENTERPRISE;
        } else if (type.equalsIgnoreCase("S")) {
            subjectType = SubjectType.SYSTEM;
        } else if (type.equalsIgnoreCase("P")) {
            subjectType = SubjectType.PERSONAL_RELATIONSHIP;
        } else {
            subjectType = SubjectType.VOIDED;
        }
        return subjectType;
    }

    /**
     *
     * @return
     */
    public int getMatchConfidencePercentage() {
        return matchConfidencePercentage;
    }

    /**
     *
     * @param matchConfidencePercentage
     */
    public void setMatchConfidencePercentage(int matchConfidencePercentage) {
        this.matchConfidencePercentage = matchConfidencePercentage;
    }

    /**
     *
     * @return
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     *
     * @param addresses
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     *
     * @param address
     */
    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    /**
     * 
     * @return
     */
    public List<TelecomAddress> getTelecomAddresses() {
        return telecomAddresses;
    }

    /**
     *
     * @param telecomAddresses
     */
    public void setTelecomAddresses(List<TelecomAddress> telecomAddresses) {
        this.telecomAddresses = telecomAddresses;
    }

    /**
     *
     * @param telecomAddress
     */
    public void addTelecomAddress(TelecomAddress telecomAddress) {
        this.telecomAddresses.add(telecomAddress);
    }

    /**
     *
     * @return
     */
    public List<SubjectIdentifier> getSubjectIdentifiers() {
        return subjectIdentifiers;
    }

    /**
     *
     * @param subjectIdentifiers
     */
    public void setSubjectIdentifiers(List<SubjectIdentifier> subjectIdentifiers) {
        this.subjectIdentifiers = subjectIdentifiers;
    }

    /**
     * 
     * @return
     */
    public List<SubjectIdentifier> getSubjectOtherIdentifiers() {
        return subjectOtherIdentifiers;
    }

    /**
     *
     * @param subjectOtherIdentifiers
     */
    public void setSubjectOtherIdentifiers(List<SubjectIdentifier> subjectOtherIdentifiers) {
        this.subjectOtherIdentifiers = subjectOtherIdentifiers;
    }

    /**
     * Add subject identifier to the list of identifiers for the subject, but only if
     * the identifier is not already on the list.
     *
     * @param subjectIdentifier
     */
    public void addSubjectIdentifier(SubjectIdentifier subjectIdentifier) {
        // First make sure that the subject identifier does not already exist in the list.
        for (SubjectIdentifier id : this.subjectIdentifiers) {
            if (subjectIdentifier.equals(id)) {
                // Already exists.
                return;  // EARLY EXIT!!
            }
        }
        this.subjectIdentifiers.add(subjectIdentifier);
    }

    /**
     * Remove subject identifier from the list of identifiers for the subject.
     *
     * @param subjectIdentifier
     */
    public void removeSubjectIdentifier(SubjectIdentifier subjectIdentifier) {
        // First make sure that the subject identifier does not already exist in the list.
        int i = 0;
        for (SubjectIdentifier id : this.subjectIdentifiers) {
            if (subjectIdentifier.equals(id)) {
                this.subjectIdentifiers.remove(i);
                break;
            }
            ++i;
        }
    }

    /**
     *
     * @return
     */
    public List<SubjectName> getSubjectNames() {
        return subjectNames;
    }

    /**
     *
     * @param subjectName
     */
    public void addSubjectName(SubjectName subjectName) {
        this.subjectNames.add(subjectName);
    }

    /**
     *
     * @param subjectNames
     */
    public void setSubjectNames(List<SubjectName> subjectNames) {
        this.subjectNames = subjectNames;
    }

    /**
     *
     * @return
     */
    public List<SubjectPersonalRelationship> getSubjectPersonalRelationships() {
        return subjectPersonalRelationships;
    }

    /**
     *
     * @param subjectPersonalRelationships
     */
    public void setSubjectPersonalRelationships(List<SubjectPersonalRelationship> subjectPersonalRelationships) {
        this.subjectPersonalRelationships = subjectPersonalRelationships;
    }

    /**
     *
     * @param subjectPersonalRelationship
     */
    public void SubjectPersonalRelationship(SubjectPersonalRelationship subjectPersonalRelationship) {
        this.subjectPersonalRelationships.add(subjectPersonalRelationship);
    }

    /**
     *
     * @return
     */
    public CodedValue getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     */
    public void setGender(CodedValue gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     */
    public CodedValue getEthnicGroup() {
        return ethnicGroup;
    }

    /**
     *
     * @param ethnicGroup
     */
    public void setEthnicGroup(CodedValue ethnicGroup) {
        this.ethnicGroup = ethnicGroup;
    }

    /**
     *
     * @return
     */
    public CodedValue getMaritalStatus() {
        return maritalStatus;
    }

    /**
     *
     * @param maritalStatus
     */
    public void setMaritalStatus(CodedValue maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     *
     * @return
     */
    public CodedValue getRace() {
        return race;
    }

    /**
     *
     * @param race
     */
    public void setRace(CodedValue race) {
        this.race = race;
    }

    /**
     *
     * @return
     */
    public CodedValue getReligiousAffiliation() {
        return religiousAffiliation;
    }

    /**
     *
     * @param religiousAffiliation
     */
    public void setReligiousAffiliation(CodedValue religiousAffiliation) {
        this.religiousAffiliation = religiousAffiliation;
    }

    /**
     *
     * @return
     */
    public Date getBirthTime() {
        return birthTime;
    }

    /**
     *
     * @param birthTime
     */
    public void setBirthTime(Date birthTime) {
        this.birthTime = birthTime;
    }

    /**
     *
     * @return
     */
    public Custodian getCustodian() {
        return custodian;
    }

    /**
     *
     * @param custodian
     */
    public void setCustodian(Custodian custodian) {
        this.custodian = custodian;
    }

    /**
     *
     * @return
     */
    public Boolean getDeceasedIndicator() {
        return deceasedIndicator;
    }

    /**
     *
     * @param deceasedIndicator
     */
    public void setDeceasedIndicator(Boolean deceasedIndicator) {
        this.deceasedIndicator = deceasedIndicator;
    }

    /**
     * 
     * @return
     */
    public Date getDeceasedTime() {
        return deceasedTime;
    }

    /**
     *
     * @param deceasedTime
     */
    public void setDeceasedTime(Date deceasedTime) {
        this.deceasedTime = deceasedTime;
    }

    /**
     *
     * @return
     */
    public Boolean getMultipleBirthIndicator() {
        return multipleBirthIndicator;
    }

    /**
     *
     * @param multipleBirthIndicator
     */
    public void setMultipleBirthIndicator(Boolean multipleBirthIndicator) {
        this.multipleBirthIndicator = multipleBirthIndicator;
    }

    /**
     *
     * @return
     */
    public Integer getMultipleBirthOrderNumber() {
        return multipleBirthOrderNumber;
    }

    /**
     *
     * @param multipleBirthOrderNumber
     */
    public void setMultipleBirthOrderNumber(Integer multipleBirthOrderNumber) {
        this.multipleBirthOrderNumber = multipleBirthOrderNumber;
    }

    /**
     * Return SubjectIdentifier in the given SubjectIdentifierDomain.  Return null if not found.
     *
     * @param identifierDomain
     * @return
     */
    public SubjectIdentifier getSubjectIdentifier(SubjectIdentifierDomain identifierDomain) {
        for (SubjectIdentifier subjectIdentifier : this.subjectIdentifiers) {
            SubjectIdentifierDomain subjectIdentifierDomain = subjectIdentifier.getIdentifierDomain();
            if (subjectIdentifierDomain.getUniversalId().equals(identifierDomain.getUniversalId())) {
                return subjectIdentifier;
            }
        }
        return null;
    }

    /**
     * 
     * @param identifier
     * @return
     */
    public boolean hasSubjectIdentifier(SubjectIdentifier identifier) {
        return this.hasIdentifier(identifier, this.getSubjectIdentifiers());
    }

    /**
     *
     * @param identifier
     * @return
     */
    public boolean hasSubjectOtherIdentifier(SubjectIdentifier identifier) {
        return this.hasIdentifier(identifier, this.getSubjectOtherIdentifiers());
    }

    /**
     *
     * @param identifier
     * @param subjectIdentifiers
     * @return
     */
    private boolean hasIdentifier(SubjectIdentifier identifier, List<SubjectIdentifier> subjectIdentifiers) {
        SubjectIdentifierDomain identifierDomain = identifier.getIdentifierDomain();
        // See if we find our subject identifier.
        for (SubjectIdentifier subjectIdentifier : subjectIdentifiers) {
            SubjectIdentifierDomain subjectIdentifierDomain = subjectIdentifier.getIdentifierDomain();
            if (subjectIdentifierDomain.getUniversalId().equals(identifierDomain.getUniversalId()) && subjectIdentifier.getIdentifier().equals(identifier.getIdentifier())) {
                return true;  // Match.
            }
        }
        return false;  // No match.
    }

    /**
     * 
     * @return
     */
    public boolean hasSubjectIdentifiers() {

        return !this.getSubjectIdentifiers().isEmpty();
    }

    /**
     *
     * @return
     */
    public boolean hasSubjectDemographics() {
        return (this.getGender() != null)
                || (this.getBirthTime() != null)
                || !this.getSubjectNames().isEmpty()
                || !this.getAddresses().isEmpty();
    }

    /**
     *
     * @return
     */
    public String getInternalId() {
        return internalId;
    }

    /**
     *
     * @param internalId
     */
    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    /**
     *
     * @return
     */
    public SubjectType getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(SubjectType type) {
        this.type = type;
    }

    /**
     *
     */
    public void clearIdentifiers() {
        subjectIdentifiers.clear();
        subjectOtherIdentifiers.clear();
    }

    /**
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Subject copy = (Subject) super.clone();
        copy.subjectIdentifiers = SubjectIdentifier.clone(subjectIdentifiers);
        copy.subjectOtherIdentifiers = SubjectIdentifier.clone(subjectOtherIdentifiers);
        copy.subjectNames = SubjectName.clone(subjectNames);
        copy.addresses = Address.clone(addresses);
        copy.telecomAddresses = TelecomAddress.clone(telecomAddresses);
        copy.subjectPersonalRelationships = SubjectPersonalRelationship.clone(subjectPersonalRelationships);
        copy.gender = CodedValue.clone(gender);
        copy.maritalStatus = CodedValue.clone(maritalStatus);
        copy.religiousAffiliation = CodedValue.clone(religiousAffiliation);
        copy.race = CodedValue.clone(race);
        copy.ethnicGroup = CodedValue.clone(ethnicGroup);
        if (custodian != null) {
            copy.custodian = (Custodian) custodian.clone();
        }
        if (birthTime != null) {
            copy.birthTime = (Date) birthTime.clone();
        }
        if (deceasedTime != null) {
            copy.deceasedTime = (Date) deceasedTime.clone();
        }
        return copy;
    }

    /**
     *
     * @param listToClone
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<Subject> clone(List<Subject> listToClone) throws CloneNotSupportedException {
        List<Subject> copy = null;
        if (listToClone != null) {
            copy = new ArrayList<Subject>();
            for (Subject elementToClone : listToClone) {
                Subject clonedElement = (Subject) elementToClone.clone();
                copy.add(clonedElement);
            }
        }
        return copy;
    }
}

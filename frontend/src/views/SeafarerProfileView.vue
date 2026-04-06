<script setup>
import { ref, computed } from "vue"
import { useRoute } from "vue-router"
import { useAuthStore } from "@/stores/auth"

const route = useRoute()
const auth = useAuthStore()
const photoMap = { 'simos.varias@email.com': '/profiles/simos-varias.jpg' }
const userPhoto = computed(() => photoMap[auth.user?.email] || null)
const activeTab = ref('overview')
const shareMode = ref(false)
const selectedDocs = ref(new Set())

function toggleShareMode() {
  shareMode.value = !shareMode.value
  if (!shareMode.value) selectedDocs.value.clear()
}

function toggleDoc(docName) {
  if (selectedDocs.value.has(docName)) selectedDocs.value.delete(docName)
  else selectedDocs.value.add(docName)
}

function selectAll() {
  profile.value.documents.forEach(cat => cat.items.forEach(doc => selectedDocs.value.add(doc.name)))
}

function selectNone() { selectedDocs.value.clear() }

const showModal = ref(false)
const modalType = ref('')
const modalForm = ref({})

const modalFields = {
  'Sea Service': [
    { key: 'rankHeld', label: 'Rank', type: 'select', options: ['Cadet','3rd Officer','2nd Officer','Chief Officer','Master'] },
    { key: 'vesselName', label: 'Vessel Name', type: 'text', placeholder: 'MT Example' },
    { key: 'vesselType', label: 'Vessel Type', type: 'select', options: ['OIL_TANKER','CHEMICAL_TANKER','OIL_CHEMICAL_TANKER','BULK_CARRIER','CONTAINER','LPG_CARRIER','LNG_CARRIER','GENERAL_CARGO'] },
    { key: 'dwt', label: 'DWT', type: 'text', placeholder: '50,000' },
    { key: 'flagState', label: 'Flag State', type: 'text', placeholder: 'Panama' },
    { key: 'companyName', label: 'Company', type: 'text', placeholder: 'Shipping Company' },
    { key: 'startDate', label: 'Start Date', type: 'date' },
    { key: 'endDate', label: 'End Date', type: 'date' },
    { key: 'description', label: 'Description', type: 'textarea', placeholder: 'Describe your duties...' }
  ],
  'Certificate': [
    { key: 'certName', label: 'Certificate Name', type: 'text', placeholder: 'e.g. STCW Basic Safety' },
    { key: 'certType', label: 'Type', type: 'select', options: ['COC','STCW','TANKER','ECDIS','MEDICAL','SSO','BRM','FLAG','OTHER'] },
    { key: 'issuingAuthority', label: 'Issuing Authority', type: 'text', placeholder: 'e.g. Hellenic Coast Guard' },
    { key: 'issueDate', label: 'Issue Date', type: 'date' },
    { key: 'expiryDate', label: 'Expiry Date', type: 'date' },
    { key: 'file', label: 'Upload Scan (PDF/Image)', type: 'file' }
  ],
  'Skill': [
    { key: 'name', label: 'Skill Name', type: 'text', placeholder: 'e.g. Cargo Operations' }
  ],
  'Award': [
    { key: 'title', label: 'Award Title', type: 'text', placeholder: 'e.g. Zero Incident Voyage Award' },
    { key: 'issuer', label: 'Issued By', type: 'text', placeholder: 'Company or organization' },
    { key: 'year', label: 'Year', type: 'text', placeholder: '2025' },
    { key: 'description', label: 'Description', type: 'textarea', placeholder: 'Describe the achievement...' }
  ],
  'Education': [
    { key: 'institutionName', label: 'Institution', type: 'text', placeholder: 'University or Academy' },
    { key: 'degree', label: 'Degree / Diploma', type: 'text', placeholder: 'BSc Marine Engineering' },
    { key: 'endYear', label: 'Graduation Year', type: 'text', placeholder: '2020' },
    { key: 'description', label: 'Description', type: 'textarea', placeholder: 'Thesis, honors, etc.' }
  ],
  'Language': [
    { key: 'name', label: 'Language', type: 'text', placeholder: 'e.g. French' },
    { key: 'level', label: 'Proficiency', type: 'select', options: ['Native','Fluent','Conversational','Basic'] }
  ],
  'Interest': [
    { key: 'name', label: 'Interest / Topic', type: 'text', placeholder: 'e.g. Green Shipping' }
  ],
  'Document': [
    { key: 'name', label: 'Document Name', type: 'text', placeholder: 'e.g. Passport' },
    { key: 'number', label: 'Document Number', type: 'text', placeholder: 'e.g. AK 2847561' },
    { key: 'issued', label: 'Issue Date', type: 'date' },
    { key: 'expires', label: 'Expiry Date', type: 'date' },
    { key: 'file', label: 'Upload Document (PDF/Image)', type: 'file' }
  ]
}

function handleUpload(section) {
  modalType.value = section
  modalForm.value = {}
  showModal.value = true
}

function submitForm() {
  const section = modalType.value
  if (section === 'Sea Service') {
    profile.value.seaServiceRecords.push({ id: Date.now().toString(), ...modalForm.value, status: 'VALID' })
  } else if (section === 'Certificate') {
    profile.value.certificates.push({ id: Date.now().toString(), ...modalForm.value, status: 'VALID', verifiedStatus: 'UNVERIFIED' })
  } else if (section === 'Skill') {
    profile.value.skills.push({ name: modalForm.value.name, endorsements: 0 })
  } else if (section === 'Award') {
    profile.value.awards.push({ ...modalForm.value })
  } else if (section === 'Education') {
    profile.value.education.push({ ...modalForm.value })
  } else if (section === 'Language') {
    profile.value.languages.push({ ...modalForm.value, flag: '' })
  } else if (section === 'Interest') {
    profile.value.interests.push(modalForm.value.name)
  }
  showModal.value = false
  modalForm.value = {}
}

const profile = ref({
  fullName: auth.user?.fullName || "Simos Varias",
  currentRank: "Chief Officer", nationality: "Greek",
  yearsOfExperience: 12, verifiedStatus: "VERIFIED", profileCompleteness: 96,
  bio: "Experienced Chief Officer with 12 years in tanker operations. Deep expertise in oil/chemical cargo handling, ECDIS navigation, and ISM/ISPS compliance. Strong leadership skills with proven track record managing bridge teams across diverse multinational crews.",
  availableFrom: "2026-07-01", desiredRank: "Master", contractPreference: "4+2",
  vesselPreferences: ["Oil/Chemical Tanker", "LPG Carrier", "LNG Carrier"],
  profileViews: 1284, shortlistCount: 47, inquiries: 8, connections: 312,
  languages: [
    { name: "Greek", level: "Native", flag: "\u{1F1EC}\u{1F1F7}" },
    { name: "English", level: "Fluent (MARLINS 92%)", flag: "\u{1F1EC}\u{1F1E7}" },
    { name: "Spanish", level: "Conversational", flag: "\u{1F1EA}\u{1F1F8}" }
  ],
  seaServiceRecords: [
    { id: "1", vesselName: "MT Aegean Warrior", vesselType: "OIL_CHEMICAL_TANKER", dwt: "75,000", flagState: "Greece", rankHeld: "Chief Officer", companyName: "Tsakos Energy Navigation", startDate: "2025-01-15", endDate: "2026-02-28", description: "Managed cargo operations for oil/chemical shipments across Mediterranean and Black Sea routes." },
    { id: "2", vesselName: "MT Poseidon Star", vesselType: "OIL_TANKER", dwt: "158,000", flagState: "Marshall Islands", rankHeld: "Chief Officer", companyName: "Diana Shipping", startDate: "2023-06-01", endDate: "2024-12-15", description: "Suezmax tanker operations on long-haul routes. Zero incidents during 18-month contract." },
    { id: "3", vesselName: "MT Mediterranean Glory", vesselType: "OIL_CHEMICAL_TANKER", dwt: "45,000", flagState: "Liberia", rankHeld: "2nd Officer", companyName: "Capital Ship Management", startDate: "2021-09-10", endDate: "2023-05-20", description: "Navigation officer responsible for passage planning and ECDIS operations." },
    { id: "4", vesselName: "MT Hellenic Pride", vesselType: "OIL_TANKER", dwt: "120,000", flagState: "Panama", rankHeld: "2nd Officer", companyName: "Angelicoussis Group", startDate: "2019-11-01", endDate: "2021-08-30", description: "Bridge watchkeeping and cargo monitoring on Aframax tanker." },
    { id: "5", vesselName: "MT Olympus Navigator", vesselType: "CHEMICAL_TANKER", dwt: "35,000", flagState: "Malta", rankHeld: "3rd Officer", companyName: "Thenamaris", startDate: "2017-03-15", endDate: "2019-10-20", description: "Junior officer on chemical tanker. Completed advanced cargo handling training." },
    { id: "6", vesselName: "MT Athena Express", vesselType: "OIL_TANKER", dwt: "95,000", flagState: "Bahamas", rankHeld: "Cadet", companyName: "Minerva Marine", startDate: "2014-09-01", endDate: "2017-02-28", description: "Cadet training covering all deck operations, safety drills, and navigation." }
  ],
  certificates: [
    { id: "1", certName: "Certificate of Competency \u2014 Chief Officer", certType: "COC", issuingAuthority: "Hellenic Coast Guard", expiryDate: "2030-12-31", issueDate: "2020-12-15", status: "VALID", verifiedStatus: "VERIFIED" },
    { id: "2", certName: "STCW Manila Amendments Full Package", certType: "STCW", issuingAuthority: "Hellenic Coast Guard", expiryDate: "2028-06-15", issueDate: "2023-06-15", status: "VALID", verifiedStatus: "VERIFIED" },
    { id: "3", certName: "Advanced Tanker Operations (Oil/Chemical/Gas)", certType: "TANKER", issuingAuthority: "AENOR Maritime Academy", expiryDate: "2027-11-30", issueDate: "2022-11-30", status: "VALID", verifiedStatus: "VERIFIED" },
    { id: "4", certName: "ECDIS Type-Specific (JRC / Furuno)", certType: "ECDIS", issuingAuthority: "Warsash Maritime School", expiryDate: "2028-03-15", issueDate: "2023-03-15", status: "VALID", verifiedStatus: "VERIFIED" },
    { id: "5", certName: "Medical First Aid (MFA P2)", certType: "MEDICAL", issuingAuthority: "Red Cross Maritime", expiryDate: "2026-09-01", issueDate: "2021-09-01", status: "EXPIRING", verifiedStatus: "VERIFIED" },
    { id: "6", certName: "Ship Security Officer (SSO)", certType: "SSO", issuingAuthority: "Lloyd's Maritime Academy", expiryDate: "2029-04-30", issueDate: "2024-04-30", status: "VALID", verifiedStatus: "VERIFIED" },
    { id: "7", certName: "Bridge Resource Management (BRM)", certType: "BRM", issuingAuthority: "NTUA Maritime Center", expiryDate: "2027-08-20", issueDate: "2022-08-20", status: "VALID", verifiedStatus: "VERIFIED" },
    { id: "8", certName: "Panama Flag Endorsement", certType: "FLAG", issuingAuthority: "Panama Maritime Authority", expiryDate: "2027-12-31", issueDate: "2022-12-31", status: "VALID", verifiedStatus: "VERIFIED" },
    { id: "9", certName: "Marshall Islands Flag Endorsement", certType: "FLAG", issuingAuthority: "Republic of Marshall Islands", expiryDate: "2028-06-30", issueDate: "2023-06-30", status: "VALID", verifiedStatus: "UNVERIFIED" },
    { id: "10", certName: "Liberia Flag Endorsement", certType: "FLAG", issuingAuthority: "Liberian Maritime Authority", expiryDate: "2027-09-15", issueDate: "2022-09-15", status: "VALID", verifiedStatus: "VERIFIED" }
  ],
  skills: [
    { name: "ECDIS", endorsements: 14 }, { name: "Cargo Operations", endorsements: 11 },
    { name: "Bridge Watch", endorsements: 9 }, { name: "ISM/ISPS", endorsements: 8 },
    { name: "Tanker Safety", endorsements: 12 }, { name: "MARPOL", endorsements: 6 },
    { name: "Oil/Chemical Handling", endorsements: 10 }, { name: "BRM", endorsements: 7 },
    { name: "Crew Management", endorsements: 5 }, { name: "Passage Planning", endorsements: 13 },
    { name: "Emergency Response", endorsements: 8 }, { name: "SOLAS", endorsements: 6 },
    { name: "Port State Inspections", endorsements: 9 }, { name: "Vetting Inspections", endorsements: 11 },
    { name: "GMDSS", endorsements: 7 }
  ],
  education: [
    { institutionName: "National Technical University of Athens", degree: "BSc Merchant Marine", endYear: 2014, description: "Graduated with honors. Thesis on ballast water treatment systems." },
    { institutionName: "Lloyd's Maritime Academy", degree: "Diploma in Ship Operations Management", endYear: 2020, description: "Fleet operations, P&I insurance, maritime commercial law." }
  ],
  awards: [
    { title: "Zero Incident Voyage Award", issuer: "Tsakos Energy Navigation", year: 2025, description: "14-month contract with zero LTIs and zero environmental incidents." },
    { title: "Best Officer of the Quarter", issuer: "Diana Shipping", year: 2024, description: "SIRE vetting inspection with zero observations." },
    { title: "Safety Excellence Certificate", issuer: "Capital Ship Management", year: 2022, description: "Outstanding contribution to onboard safety culture." }
  ],
  references: [
    { name: "Capt. Georgios Papandreou", title: "Fleet Manager, Tsakos Energy", relation: "Direct supervisor (2 contracts)", available: true },
    { name: "Capt. Nikolaos Dimitriou", title: "Master, Diana Shipping", relation: "Served as C/O under his command", available: true },
    { name: "Eng. Alexandros Mavros", title: "Tech Supt, Capital Ship Mgmt", relation: "Dry-docking collaboration", available: false }
  ],
  interests: ["Maritime Safety", "Decarbonization", "Crew Welfare", "ECDIS Technology", "Autonomous Vessels", "Greek Maritime History"],
  documents: [
    { category: "Identity & Travel", items: [
      { name: "Passport", number: "AK 2847561", issued: "2022-03-10", expires: "2032-03-09", status: "VALID", shared: true },
      { name: "Seaman's Book (Continuous Discharge Certificate)", number: "CDC-2014-08421", issued: "2014-09-01", expires: "2029-09-01", status: "VALID", shared: true },
      { name: "National ID Card", number: "AI 284756", issued: "2020-01-15", expires: "2030-01-15", status: "VALID", shared: false },
      { name: "Visa - USA (C1/D Crewman)", number: "US-C1D-2024", issued: "2024-02-20", expires: "2029-02-20", status: "VALID", shared: true },
      { name: "Visa - Schengen (Multiple Entry)", number: "SCH-2023-GR", issued: "2023-06-01", expires: "2028-06-01", status: "VALID", shared: false }
    ]},
    { category: "Medical & PEME", items: [
      { name: "PEME (Pre-Employment Medical Exam)", number: "PEME-2026-0412", issued: "2026-02-15", expires: "2028-02-15", status: "VALID", shared: true },
      { name: "Medical Fitness Certificate", number: "MFC-2026-GR", issued: "2026-02-15", expires: "2028-02-15", status: "VALID", shared: true },
      { name: "Drug & Alcohol Test", number: "DAT-2026-0215", issued: "2026-02-15", expires: "2026-08-15", status: "EXPIRING", shared: true },
      { name: "Yellow Fever Vaccination", number: "YFV-2023", issued: "2023-04-10", expires: "Life", status: "VALID", shared: true },
      { name: "COVID-19 Vaccination Record", number: "VAX-GR-2021", issued: "2021-06-20", expires: "N/A", status: "VALID", shared: true },
      { name: "Eyesight & Color Vision Test", number: "EYE-2026-02", issued: "2026-02-15", expires: "2028-02-15", status: "VALID", shared: false },
      { name: "Chest X-Ray Report", number: "XR-2026-02", issued: "2026-02-15", expires: "2028-02-15", status: "VALID", shared: false },
      { name: "Blood Test (CBC, Hepatitis, HIV)", number: "BT-2026-02", issued: "2026-02-15", expires: "2028-02-15", status: "VALID", shared: false }
    ]},
    { category: "Competency & STCW Certificates", items: [
      { name: "COC - Chief Officer (Deck)", number: "COC-GR-2020-4821", issued: "2020-12-15", expires: "2030-12-31", status: "VALID", shared: true },
      { name: "STCW Full Package (Manila Amendments)", number: "STCW-GR-2023", issued: "2023-06-15", expires: "2028-06-15", status: "VALID", shared: true },
      { name: "Advanced Tanker Cargo Operations", number: "ATCO-2022", issued: "2022-11-30", expires: "2027-11-30", status: "VALID", shared: true },
      { name: "ECDIS Type-Specific (JRC/Furuno)", number: "ECDIS-2023", issued: "2023-03-15", expires: "2028-03-15", status: "VALID", shared: true },
      { name: "Ship Security Officer (SSO)", number: "SSO-2024", issued: "2024-04-30", expires: "2029-04-30", status: "VALID", shared: true },
      { name: "Bridge Resource Management (BRM)", number: "BRM-2022", issued: "2022-08-20", expires: "2027-08-20", status: "VALID", shared: true },
      { name: "Medical First Aid (MFA P2)", number: "MFA-2021", issued: "2021-09-01", expires: "2026-09-01", status: "EXPIRING", shared: true },
      { name: "Proficiency in Survival Craft (PSC)", number: "PSC-2023", issued: "2023-06-15", expires: "2028-06-15", status: "VALID", shared: true },
      { name: "Advanced Fire Fighting (AFF)", number: "AFF-2023", issued: "2023-06-15", expires: "2028-06-15", status: "VALID", shared: true }
    ]},
    { category: "Flag State Endorsements", items: [
      { name: "Panama Flag Endorsement", number: "PAN-2022-8842", issued: "2022-12-31", expires: "2027-12-31", status: "VALID", shared: true },
      { name: "Marshall Islands Flag Endorsement", number: "MHL-2023-1204", issued: "2023-06-30", expires: "2028-06-30", status: "VALID", shared: true },
      { name: "Liberia Flag Endorsement", number: "LBR-2022-7721", issued: "2022-09-15", expires: "2027-09-15", status: "VALID", shared: true },
      { name: "Malta Flag Endorsement", number: "MLT-2023-0331", issued: "2023-03-31", expires: "2028-03-31", status: "VALID", shared: true },
      { name: "Bahamas Flag Endorsement", number: "BHS-2022-1102", issued: "2022-11-02", expires: "2027-11-02", status: "VALID", shared: true }
    ]},
    { category: "Contract & Employment", items: [
      { name: "Letter of Recommendation - Tsakos Energy", number: "LOR-TSK-2026", issued: "2026-03-01", expires: "N/A", status: "VALID", shared: true },
      { name: "Letter of Recommendation - Diana Shipping", number: "LOR-DIA-2025", issued: "2025-01-10", expires: "N/A", status: "VALID", shared: true },
      { name: "Certificate of Sea Service (Consolidated)", number: "CSS-2026", issued: "2026-03-15", expires: "N/A", status: "VALID", shared: true },
      { name: "Insurance Certificate (P&I Club)", number: "PI-2026-GR", issued: "2026-01-01", expires: "2027-01-01", status: "VALID", shared: false },
      { name: "Bank Details & Allotment Form", number: "BANK-GR-2026", issued: "2026-01-15", expires: "N/A", status: "VALID", shared: false },
      { name: "Next of Kin Declaration", number: "NOK-2026", issued: "2026-01-15", expires: "N/A", status: "VALID", shared: false }
    ]}
  ]
})
</script>

<template>
  <div class="container profile-layout">
    <main class="profile-main">
      <div class="card hero-card">
        <div class="hero-banner"></div>
        <div class="hero-body">
          <img v-if="userPhoto" :src="userPhoto" class="hero-avatar-img" />
          <div v-else class="avatar avatar-xl avatar-seafarer hero-avatar">{{ profile.fullName?.split(' ').map(w=>w[0]).join('').substring(0,2) }}</div>
          <div class="hero-info">
            <h1>{{ profile.fullName }} <span v-if="profile.verifiedStatus==='VERIFIED'" class="badge badge-verified">&#10003; Verified</span></h1>
            <p class="hero-headline">{{ profile.currentRank }} | {{ profile.nationality }} | {{ profile.yearsOfExperience }} years sea service</p>
            <p class="hero-bio">{{ profile.bio }}</p>
            <div class="hero-stats">
              <span>&#128065; {{ profile.profileViews.toLocaleString() }} views</span>
              <span>&#128203; {{ profile.shortlistCount }} shortlists</span>
              <span>&#128172; {{ profile.inquiries }} inquiries</span>
              <span>&#128279; {{ profile.connections }} connections</span>
            </div>
          </div>
          <div class="hero-actions">
            <button class="btn btn-primary">+ Add to Shortlist</button>
            <button class="btn btn-secondary">Message</button>
            <button class="btn btn-tertiary">Download CV</button>
          </div>
        </div>
      </div>

      <div class="tabs-bar">
        <button :class="{ active: activeTab === 'overview' }" @click="activeTab = 'overview'">Overview</button>
        <button :class="{ active: activeTab === 'service' }" @click="activeTab = 'service'">Sea Service ({{ profile.seaServiceRecords.length }})</button>
        <button :class="{ active: activeTab === 'certs' }" @click="activeTab = 'certs'">Certificates ({{ profile.certificates.length }})</button>
        <button :class="{ active: activeTab === 'skills' }" @click="activeTab = 'skills'">Skills & Awards</button>
        <button :class="{ active: activeTab === 'docs' }" @click="activeTab = 'docs'">Documents ({{ profile.documents.reduce((a,c) => a + c.items.length, 0) }})</button>
      </div>

      <template v-if="activeTab === 'overview'">
        <div class="card section"><div class="section-header"><h2>Sea Service Record</h2><button class="btn-upload" @click="handleUpload('Sea Service')">+ Add</button></div>
          <div v-for="ss in profile.seaServiceRecords.slice(0,3)" :key="ss.id" class="timeline-item">
            <div class="timeline-dot" :class="ss.rankHeld==='Cadet'?'dot-cadet':ss.rankHeld.includes('Chief')?'dot-chief':'dot-officer'"></div>
            <div class="timeline-content"><strong>{{ ss.rankHeld }} &mdash; {{ ss.vesselName }}</strong>
              <p>{{ ss.vesselType?.replace(/_/g,' ') }} | {{ ss.dwt }} DWT | {{ ss.flagState }}</p>
              <p class="desc">{{ ss.description }}</p>
              <p class="date">{{ ss.startDate }} &ndash; {{ ss.endDate }} | {{ ss.companyName }}</p></div>
          </div>
          <button class="show-more" @click="activeTab='service'">Show all {{ profile.seaServiceRecords.length }} records &rarr;</button>
        </div>
        <div class="card section"><div class="section-header"><h2>Certificates &amp; Licenses</h2><button class="btn-upload" @click="handleUpload('Certificate')">+ Upload</button></div>
          <div v-for="c in profile.certificates.slice(0,5)" :key="c.id" class="cert-row">
            <div class="cert-info"><strong>{{ c.certName }}</strong><p>{{ c.issuingAuthority }} | Expires: {{ c.expiryDate }}</p></div>
            <div class="cert-badges">
              <span class="badge" :class="c.status==='VALID'?'badge-verified':'badge-warning'">{{ c.status }}</span>
              <span v-if="c.verifiedStatus==='VERIFIED'" class="badge badge-verified">&#10003; AI Verified</span></div>
          </div>
          <button class="show-more" @click="activeTab='certs'">Show all {{ profile.certificates.length }} certificates &rarr;</button>
        </div>
        <div class="card section"><div class="section-header"><h2>Skills &amp; Endorsements</h2><button class="btn-upload" @click="handleUpload('Skill')">+ Add</button></div>
          <div class="skills-grid"><span v-for="s in profile.skills.slice(0,8)" :key="s.name" class="skill-tag">{{ s.name }} <small>&middot; {{ s.endorsements }}</small></span></div>
          <button class="show-more" @click="activeTab='skills'">Show all {{ profile.skills.length }} skills &rarr;</button>
        </div>
        <div class="card section"><div class="section-header"><h2>Awards &amp; Recognition</h2><button class="btn-upload" @click="handleUpload('Award')">+ Add</button></div>
          <div v-for="a in profile.awards" :key="a.title" class="award-item">
            <span class="award-icon">&#127942;</span><div><strong>{{ a.title }}</strong><p>{{ a.issuer }} &middot; {{ a.year }}</p><p class="desc">{{ a.description }}</p></div>
          </div>
        </div>
      </template>

      <template v-if="activeTab === 'service'">
        <div class="card section"><div class="section-header"><h2>Complete Sea Service Record</h2><button class="btn-upload" @click="handleUpload('Sea Service')">+ Add Record</button></div>
          <p class="section-sub">{{ profile.seaServiceRecords.length }} vessels | {{ profile.yearsOfExperience }} years | 4 ranks held</p>
          <div v-for="ss in profile.seaServiceRecords" :key="ss.id" class="timeline-item">
            <div class="timeline-dot" :class="ss.rankHeld==='Cadet'?'dot-cadet':ss.rankHeld.includes('Chief')?'dot-chief':'dot-officer'"></div>
            <div class="timeline-content"><strong>{{ ss.rankHeld }} &mdash; {{ ss.vesselName }}</strong>
              <p>{{ ss.vesselType?.replace(/_/g,' ') }} | {{ ss.dwt }} DWT | {{ ss.flagState }}</p>
              <p class="desc">{{ ss.description }}</p>
              <p class="date">{{ ss.startDate }} &ndash; {{ ss.endDate }} | {{ ss.companyName }}</p></div>
          </div>
        </div>
      </template>

      <template v-if="activeTab === 'certs'">
        <div class="card section"><div class="section-header"><h2>All Certificates &amp; Licenses</h2><button class="btn-upload" @click="handleUpload('Certificate')">+ Upload Certificate</button></div>
          <p class="section-sub">{{ profile.certificates.filter(c=>c.status==='VALID').length }} valid | {{ profile.certificates.filter(c=>c.status==='EXPIRING').length }} expiring | {{ profile.certificates.filter(c=>c.verifiedStatus==='VERIFIED').length }} AI verified</p>
          <div v-for="c in profile.certificates" :key="c.id" class="cert-row">
            <div class="cert-info"><strong>{{ c.certName }}</strong><p>{{ c.issuingAuthority }}</p><p class="date">Issued: {{ c.issueDate }} | Expires: {{ c.expiryDate }}</p></div>
            <div class="cert-badges">
              <span class="badge" :class="c.status==='VALID'?'badge-verified':'badge-warning'">{{ c.status }}</span>
              <span v-if="c.verifiedStatus==='VERIFIED'" class="badge badge-verified">&#10003; AI Verified</span>
              <span v-else class="badge badge-pending">Pending</span></div>
          </div>
        </div>
      </template>

      <template v-if="activeTab === 'skills'">
        <div class="card section"><div class="section-header"><h2>Skills &amp; Endorsements</h2><button class="btn-upload" @click="handleUpload('Skill')">+ Add</button></div>
          <div class="skills-list">
            <div v-for="s in profile.skills" :key="s.name" class="skill-item">
              <div class="skill-info"><strong>{{ s.name }}</strong><span>{{ s.endorsements }}</span></div>
              <div class="skill-bar"><div class="skill-fill" :style="{width: Math.min(s.endorsements*7,100)+'%'}"></div></div>
            </div>
          </div>
        </div>
        <div class="card section"><div class="section-header"><h2>Awards &amp; Recognition</h2><button class="btn-upload" @click="handleUpload('Award')">+ Add</button></div>
          <div v-for="a in profile.awards" :key="a.title" class="award-item">
            <span class="award-icon">&#127942;</span><div><strong>{{ a.title }}</strong><p>{{ a.issuer }} &middot; {{ a.year }}</p><p class="desc">{{ a.description }}</p></div>
          </div>
        </div>
        <div class="card section"><div class="section-header"><h2>Education</h2><button class="btn-upload" @click="handleUpload('Education')">+ Add</button></div>
          <div v-for="e in profile.education" :key="e.institutionName" class="edu-item">
            <span class="award-icon">&#127891;</span><div><strong>{{ e.institutionName }}</strong><p>{{ e.degree }} ({{ e.endYear }})</p><p class="desc">{{ e.description }}</p></div>
          </div>
        </div>
        <div class="card section"><div class="section-header"><h2>Languages</h2><button class="btn-upload" @click="handleUpload('Language')">+ Add</button></div>
          <div v-for="l in profile.languages" :key="l.name" class="lang-item">
            <span style="font-size:20px">{{ l.flag }}</span><div><strong>{{ l.name }}</strong><span class="lang-level">{{ l.level }}</span></div>
          </div>
        </div>
        <div class="card section"><div class="section-header"><h2>Interests</h2><button class="btn-upload" @click="handleUpload('Interest')">+ Add</button></div>
          <div class="skills-grid"><span v-for="i in profile.interests" :key="i" class="badge badge-info">{{ i }}</span></div>
        </div>
      </template>

      <template v-if="activeTab === 'docs'">
        <div class="card section docs-header">
          <div class="docs-header-row">
            <div>
              <h2>Document Vault</h2>
              <p class="section-sub">{{ profile.documents.reduce((a,c) => a + c.items.length, 0) }} documents | {{ profile.documents.reduce((a,c) => a + c.items.filter(i=>i.shared).length, 0) }} shared with principals | {{ profile.documents.reduce((a,c) => a + c.items.filter(i=>i.status==='EXPIRING').length, 0) }} expiring soon</p>
            </div>
            <div class="docs-actions">
              <template v-if="shareMode">
                <span class="share-count">{{ selectedDocs.size }} selected</span>
                <button class="btn-link" @click="selectAll">Select All</button>
                <button class="btn-link" @click="selectNone">Clear</button>
                <button class="btn btn-primary btn-sm" @click="shareDocs" :disabled="selectedDocs.size === 0">Share Selected</button>
                <button class="btn btn-tertiary btn-sm" @click="toggleShareMode">Cancel</button>
              </template>
<button class="btn-upload" @click="handleUpload('Document')">+ Upload</button>
              <button class="btn btn-primary btn-sm" @click="toggleShareMode">Share with Principal</button>
            </div>
          </div>
        </div>
        <div v-for="cat in profile.documents" :key="cat.category" class="card section">
          <h2 class="doc-cat-title">
            <span class="doc-cat-icon">{{ cat.category.includes('Identity') ? '&#128274;' : cat.category.includes('Medical') ? '&#129658;' : cat.category.includes('Competency') ? '&#127891;' : cat.category.includes('Flag') ? '&#127988;' : '&#128196;' }}</span>
            {{ cat.category }}
            <span class="doc-count">{{ cat.items.length }}</span>
          </h2>
          <div class="doc-table">
            <div class="doc-table-head">
              <span v-if="shareMode" class="doc-col-check"></span>
              <span class="doc-col-name">Document</span>
              <span class="doc-col-num">Number</span>
              <span class="doc-col-date">Issued</span>
              <span class="doc-col-date">Expires</span>
              <span class="doc-col-status">Status</span>
              <span v-if="!shareMode" class="doc-col-share">Shared</span>
            </div>
            <div v-for="doc in cat.items" :key="doc.name" class="doc-row" :class="{'doc-expiring': doc.status === 'EXPIRING', 'doc-selected': selectedDocs.has(doc.name)}" @click="shareMode ? toggleDoc(doc.name) : null" :style="shareMode ? 'cursor:pointer' : ''">
              <span v-if="shareMode" class="doc-col-check"><input type="checkbox" :checked="selectedDocs.has(doc.name)" @click.stop="toggleDoc(doc.name)" class="doc-checkbox" /></span>
              <span class="doc-col-name"><strong>{{ doc.name }}</strong></span>
              <span class="doc-col-num doc-mono">{{ doc.number }}</span>
              <span class="doc-col-date">{{ doc.issued }}</span>
              <span class="doc-col-date">{{ doc.expires }}</span>
              <span class="doc-col-status"><span class="badge" :class="doc.status==='VALID'?'badge-verified':'badge-warning'">{{ doc.status }}</span></span>
              <span v-if="!shareMode" class="doc-col-share"><span v-if="doc.shared" class="doc-shared">&#10003;</span><span v-else class="doc-private">&#128274;</span></span>
            </div>
          </div>
        </div>
      </template>

      <!-- Modal Form -->
      <teleport to="body">
        <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
          <div class="modal-box">
            <div class="modal-header">
              <h2>Add {{ modalType }}</h2>
              <button class="modal-close" @click="showModal = false">&times;</button>
            </div>
            <div class="modal-body">
              <div v-for="field in modalFields[modalType]" :key="field.key" class="form-group">
                <label>{{ field.label }}</label>
                <input v-if="field.type === 'text'" type="text" v-model="modalForm[field.key]" :placeholder="field.placeholder" class="form-input" />
                <input v-else-if="field.type === 'date'" type="date" v-model="modalForm[field.key]" class="form-input" />
                <select v-else-if="field.type === 'select'" v-model="modalForm[field.key]" class="form-input">
                  <option value="" disabled selected>Select...</option>
                  <option v-for="opt in field.options" :key="opt" :value="opt">{{ opt.replace(/_/g, ' ') }}</option>
                </select>
                <textarea v-else-if="field.type === 'textarea'" v-model="modalForm[field.key]" :placeholder="field.placeholder" class="form-input form-textarea" rows="3"></textarea>
                <div v-else-if="field.type === 'file'" class="file-upload">
                  <input type="file" accept=".pdf,.jpg,.jpeg,.png" class="file-input" />
                  <span class="file-label">Choose file (PDF, JPG, PNG)</span>
                </div>
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-tertiary" @click="showModal = false">Cancel</button>
              <button class="btn btn-primary" @click="submitForm">Save {{ modalType }}</button>
            </div>
          </div>
        </div>
      </teleport>
    </main>

    <aside class="profile-sidebar">
      <div class="card sb"><h3>AI Compatibility</h3><div class="ai-ring">92</div><p class="sb-note">Based on your search criteria</p></div>
      <div class="card sb"><h3>Availability</h3>
        <div class="av-grid"><div><label>Available from</label><strong>{{ profile.availableFrom }}</strong></div><div><label>Contract pref.</label><strong>{{ profile.contractPreference }}</strong></div></div>
        <div class="av-grid" style="margin-top:8px"><div><label>Desired rank</label><strong>{{ profile.desiredRank }}</strong></div><div><label>Vessel pref.</label><strong style="font-size:11px">{{ profile.vesselPreferences?.[0] }}</strong></div></div>
      </div>
      <div class="card sb"><h3>Languages</h3><div v-for="l in profile.languages" :key="l.name" class="sb-lang"><span>{{ l.flag }} {{ l.name }}</span><span class="sb-lvl">{{ l.level }}</span></div></div>
      <div class="card sb"><h3>Education</h3><div v-for="e in profile.education" :key="e.institutionName" style="margin-bottom:8px"><strong style="font-size:12px">{{ e.institutionName }}</strong><p style="font-size:11px;color:var(--color-text-secondary)">{{ e.degree }} ({{ e.endYear }})</p></div></div>
      <div class="card sb"><h3>References</h3>
        <div v-for="r in profile.references" :key="r.name" class="sb-ref"><strong>{{ r.name }}</strong><p>{{ r.title }}</p><span :class="r.available?'ref-on':'ref-off'">{{ r.available ? 'Available' : 'Unavailable' }}</span></div>
      </div>
      <div class="card sb"><h3>Profile Completeness</h3><div class="prog"><div class="prog-fill" :style="{width:profile.profileCompleteness+'%'}"></div></div><p class="sb-note">{{ profile.profileCompleteness }}%</p></div>
    </aside>
  </div>
</template>

<style scoped>
.profile-layout{display:grid;grid-template-columns:1fr 280px;gap:16px;padding-top:16px;padding-bottom:48px}
.hero-card{overflow:hidden}.hero-banner{height:120px;background:linear-gradient(135deg,#004182 0%,#0A66C2 40%,#1a7fd4 70%,#004182 100%)}
.hero-body{padding:16px 20px;display:flex;gap:16px;align-items:flex-start;flex-wrap:wrap}
.hero-avatar{margin-top:-40px;border:3px solid #fff}
.hero-avatar-img{width:88px;height:88px;border-radius:50%;object-fit:cover;border:3px solid white;margin-top:-40px;box-shadow:0 2px 8px rgba(0,0,0,.15)}
.hero-info{flex:1;min-width:200px}.hero-info h1{font-size:20px;font-weight:600;display:flex;align-items:center;gap:8px;flex-wrap:wrap}
.hero-headline{font-size:14px;color:var(--color-text-secondary);margin-top:4px}
.hero-bio{font-size:13px;color:var(--color-text-secondary);margin-top:8px;line-height:1.5}
.hero-stats{display:flex;gap:16px;margin-top:8px;font-size:12px;color:var(--color-text-secondary);flex-wrap:wrap}
.hero-actions{display:flex;gap:8px;flex-wrap:wrap;align-self:flex-start;margin-top:8px}
.tabs-bar{display:flex;background:white;border:1px solid var(--color-border);border-radius:var(--radius-lg);margin-top:12px;overflow:hidden}
.tabs-bar button{flex:1;padding:12px 16px;border:none;background:none;font-size:13px;font-weight:500;color:var(--color-text-secondary);cursor:pointer;border-bottom:2px solid transparent;transition:all .2s}
.tabs-bar button:hover{background:var(--color-surface)}.tabs-bar button.active{color:var(--color-primary);border-bottom-color:var(--color-primary);background:rgba(10,102,194,.04)}
.section{padding:16px 20px;margin-top:12px}.section-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:16px}
.section-header h2{margin-bottom:0}
.btn-upload{background:none;border:1.5px solid var(--color-primary);color:var(--color-primary);font-size:12px;font-weight:500;padding:6px 14px;border-radius:20px;cursor:pointer;transition:all .2s;white-space:nowrap}
.btn-upload:hover{background:var(--color-primary);color:white}
.section h2{font-size:16px;font-weight:600;margin-bottom:16px}
.section-sub{font-size:12px;color:var(--color-text-tertiary);margin-top:-12px;margin-bottom:16px}
.timeline-item{display:flex;gap:12px;margin-bottom:20px;padding-left:4px}
.timeline-dot{width:12px;height:12px;border-radius:50%;margin-top:4px;flex-shrink:0}
.dot-officer{background:var(--color-primary)}.dot-chief{background:#1B5E20}.dot-cadet{background:var(--color-accent)}
.timeline-content{flex:1}.timeline-content strong{font-size:14px;font-weight:500}
.timeline-content p{font-size:12px;color:var(--color-text-secondary);margin-top:2px}
.desc{font-size:12px;color:var(--color-text-secondary);margin-top:4px;line-height:1.4;font-style:italic}
.date{font-size:11px;color:var(--color-text-tertiary)}
.cert-row{display:flex;justify-content:space-between;align-items:center;padding:10px 0;border-bottom:1px solid var(--color-border)}
.cert-row:last-of-type{border-bottom:none}
.cert-info strong{font-size:13px;font-weight:500}.cert-info p{font-size:11px;color:var(--color-text-secondary)}
.cert-badges{display:flex;gap:6px;flex-shrink:0}
.badge-pending{background:var(--color-surface);color:var(--color-text-tertiary);border:1px solid var(--color-border);font-size:10px;padding:2px 8px;border-radius:8px}
.skills-grid{display:flex;flex-wrap:wrap;gap:8px}
.skill-tag{background:#E6F1FB;color:var(--color-primary);padding:6px 12px;border-radius:16px;font-size:12px;font-weight:500}
.skill-tag small{color:var(--color-text-tertiary);font-weight:400}
.skills-list{display:flex;flex-direction:column;gap:12px}
.skill-item{display:flex;align-items:center;gap:12px}
.skill-info{min-width:160px;display:flex;justify-content:space-between}.skill-info strong{font-size:13px}.skill-info span{font-size:11px;color:var(--color-text-tertiary)}
.skill-bar{flex:1;height:6px;background:var(--color-surface);border-radius:3px;overflow:hidden}
.skill-fill{height:100%;background:var(--color-primary);border-radius:3px}
.award-item{display:flex;gap:12px;margin-bottom:16px}.award-icon{font-size:24px;flex-shrink:0}
.award-item strong{font-size:13px}.award-item p{font-size:12px;color:var(--color-text-secondary)}
.edu-item{display:flex;gap:12px;margin-bottom:16px}
.lang-item{display:flex;gap:12px;align-items:center;margin-bottom:12px}
.lang-item strong{font-size:13px}.lang-level{font-size:11px;color:var(--color-text-tertiary);margin-left:8px}
.show-more{background:none;border:none;color:var(--color-primary);font-size:13px;font-weight:500;cursor:pointer;padding:8px 0;margin-top:4px}
.show-more:hover{text-decoration:underline}
.profile-sidebar{display:flex;flex-direction:column;gap:12px}
.sb{padding:16px}.sb h3{font-size:14px;font-weight:600;margin-bottom:12px}
.sb-note{text-align:center;font-size:11px;color:var(--color-text-secondary)}
.ai-ring{width:64px;height:64px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-size:20px;font-weight:700;margin:8px auto;border:2.5px solid #1B5E20;color:#1B5E20}
.av-grid{display:grid;grid-template-columns:1fr 1fr;gap:8px}
.av-grid label{font-size:10px;color:var(--color-text-tertiary);display:block}.av-grid strong{font-size:13px}
.sb-lang{display:flex;justify-content:space-between;align-items:center;padding:4px 0;font-size:12px}
.sb-lvl{font-size:10px;color:var(--color-text-tertiary)}
.sb-ref{margin-bottom:12px;padding-bottom:12px;border-bottom:1px solid var(--color-border)}
.sb-ref:last-child{border-bottom:none;margin-bottom:0;padding-bottom:0}
.sb-ref strong{font-size:12px}.sb-ref p{font-size:11px;color:var(--color-text-secondary)}
.ref-on{font-size:10px;color:#1B5E20}.ref-off{font-size:10px;color:var(--color-text-tertiary)}
.prog{height:8px;background:var(--color-surface);border-radius:4px;overflow:hidden}
.prog-fill{height:100%;background:var(--color-primary);border-radius:4px}
@media(max-width:768px){.profile-layout{grid-template-columns:1fr}}
.modal-overlay{position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.5);display:flex;align-items:center;justify-content:center;z-index:1000;padding:20px}
.modal-box{background:white;border-radius:12px;width:100%;max-width:560px;max-height:85vh;overflow-y:auto;box-shadow:0 20px 60px rgba(0,0,0,0.3)}
.modal-header{display:flex;justify-content:space-between;align-items:center;padding:20px 24px;border-bottom:1px solid var(--color-border)}
.modal-header h2{font-size:18px;font-weight:600;margin:0}
.modal-close{background:none;border:none;font-size:24px;cursor:pointer;color:var(--color-text-secondary);padding:4px 8px;border-radius:4px}
.modal-close:hover{background:var(--color-surface)}
.modal-body{padding:24px}
.modal-footer{padding:16px 24px;border-top:1px solid var(--color-border);display:flex;justify-content:flex-end;gap:8px}
.form-group{margin-bottom:16px}
.form-group label{display:block;font-size:13px;font-weight:500;color:var(--color-text-primary);margin-bottom:6px}
.form-input{width:100%;padding:10px 12px;border:1px solid var(--color-border);border-radius:8px;font-size:13px;font-family:inherit;transition:border-color 0.2s;box-sizing:border-box}
.form-input:focus{outline:none;border-color:var(--color-primary);box-shadow:0 0 0 3px rgba(10,102,194,0.1)}
.form-textarea{resize:vertical;min-height:80px}
.file-upload{position:relative;border:2px dashed var(--color-border);border-radius:8px;padding:20px;text-align:center;cursor:pointer;transition:border-color 0.2s}
.file-upload:hover{border-color:var(--color-primary)}
.file-input{position:absolute;top:0;left:0;width:100%;height:100%;opacity:0;cursor:pointer}
.file-label{font-size:13px;color:var(--color-text-secondary)}
.docs-header-row{display:flex;justify-content:space-between;align-items:flex-start;gap:16px;flex-wrap:wrap}
.doc-cat-title{display:flex;align-items:center;gap:8px}
.doc-cat-icon{font-size:20px}
.doc-count{background:var(--color-surface);color:var(--color-text-secondary);font-size:11px;padding:2px 8px;border-radius:10px;font-weight:400}
.doc-table{margin-top:12px}
.doc-table-head,.doc-row{grid-template-columns:2fr 1.2fr 0.8fr 0.8fr 0.7fr 0.5fr}
.doc-row:last-child{border-bottom:none}
.doc-row strong{font-size:12px;font-weight:500}
.doc-row:hover{background:rgba(0,0,0,0.02)}
.doc-col-name{overflow:hidden;text-overflow:ellipsis}
.doc-col-num,.doc-col-date{color:var(--color-text-secondary);font-size:11px}
.doc-mono{font-family:monospace;font-size:10px}
.doc-expiring{background:rgba(230,81,0,0.04);border-radius:4px;padding-left:4px;padding-right:4px}
.doc-shared{color:#1B5E20;font-weight:bold}
.doc-private{font-size:10px;color:var(--color-text-tertiary)}
.docs-actions{display:flex;gap:8px;align-items:center;flex-wrap:wrap}
.btn-sm{font-size:12px;padding:8px 16px}
.btn-link{background:none;border:none;color:var(--color-primary);font-size:12px;cursor:pointer;padding:4px 8px;font-weight:500}
.btn-link:hover{text-decoration:underline}
.share-count{font-size:12px;font-weight:600;color:var(--color-primary);padding:4px 8px;background:#E6F1FB;border-radius:12px}
.doc-col-check{width:32px;display:flex;align-items:center;justify-content:center}
.doc-checkbox{width:16px;height:16px;cursor:pointer;accent-color:var(--color-primary)}
.doc-selected{background:rgba(10,102,194,0.06);border-radius:4px}
.doc-table-head{display:grid;gap:8px;padding:8px 0;border-bottom:2px solid var(--color-border);font-size:10px;font-weight:600;color:var(--color-text-tertiary);text-transform:uppercase;letter-spacing:0.5px}
.doc-row{display:grid;gap:8px;padding:10px 0;border-bottom:1px solid var(--color-border);align-items:center;font-size:12px;transition:background 0.15s}
</style>


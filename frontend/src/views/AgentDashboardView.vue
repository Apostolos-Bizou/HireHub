<script setup>
import { ref, computed } from 'vue'
const activeTab = ref(0)
const selectedRequest = ref(0)
const tabs = [
  {name:'Crew calendar', tip:'Ημερολόγιο πληρώματος ανά πλοίο — Gantt chart με συμβόλαια, αντικαταστάσεις, κόστη'},
  {name:'AI intelligence', tip:'Εργαλεία τεχνητής νοημοσύνης — υπολογισμός κόστους, σύγκριση υποψηφίων, χρονοδιάγραμμα'},
  {name:'Principal matching', tip:'AI αντιστοίχιση — ποιος ναυτικός ταιριάζει με ποιον πλοιοκτήτη βάσει ιστορικού'},
  {name:'Shortlist analyzer', tip:'Ανάλυση εισερχόμενων shortlists — ετοιμότητα υποψηφίων ανά αίτημα πλοιοκτήτη'},
  {name:'Deployment readiness', tip:'Ετοιμότητα deployment — πιστοποιητικά, σημαίες, ιατρικά, διαθεσιμότητα ανά ναυτικό'},
  {name:'Compliance', tip:'Ειδοποιήσεις συμμόρφωσης — ληγμένα πιστοποιητικά, DMW, απόδοση pipeline'}
]
const expandedCandidate = ref(null)
const selPrincipal = ref(0)
const selVessel = ref(0)

const shortlists = [
  { owner:'Varship Shipping Co.', i:'VS', color:'#E7A33E', time:'2h ago', urgency:'Urgent', position:'3rd Officer — Oil Tanker · MT Varship Glory · Crew change May 15', readyPct:67,
    candidates:[{n:'Juan Dela Cruz',i:'JD',c:'#0A66C2',s:'Ready',sc:'r-green'},{n:'Ruslan Goncharov',i:'RG',c:'#E7A33E',s:'Flag needed',sc:'r-yellow'},{n:'Lazar Stoyanov',i:'LS',c:'#B71C1C',s:'STCW expired',sc:'r-red'}]},
  { owner:'Diana Shipping', i:'DS', color:'#0A66C2', time:'Yesterday', urgency:'Standard', position:'Chief Engineer — Bulk Carrier · MV Diana Explorer · Crew change Jun 1', readyPct:80,
    candidates:[{n:'Dmitry Petrov',i:'DP',c:'#1D9E75',s:'Ready',sc:'r-green'},{n:'Raj Patel',i:'RP',c:'#1D9E75',s:'Ready',sc:'r-green'},{n:'Miguel Santos',i:'MS',c:'#1D9E75',s:'Ready',sc:'r-green'},{n:'Chen Wei',i:'CW',c:'#E7A33E',s:'Medical due',sc:'r-yellow'}]}
]
const readiness = [
  { n:'Juan Dela Cruz',i:'JD',c:'#0A66C2',rank:'3rd Officer',nat:'Filipino',yrs:4,ai:92,cr:87,dr:96,s:'Ready',sc:'r-green',
    checks:[{t:'COC valid until Dec 2030',o:'g'},{t:'STCW valid until Aug 2028',o:'g'},{t:'Panama flag endorsement active',o:'g'},{t:'Medical valid until Sep 2026',o:'g'},{t:'Available May 2026',o:'g'}]},
  { n:'Ruslan Goncharov',i:'RG',c:'#E7A33E',rank:'2nd Officer',nat:'Ukrainian',yrs:6,ai:87,cr:82,dr:68,s:'Action needed',sc:'r-yellow',
    checks:[{t:'COC valid until Mar 2029',o:'g'},{t:'STCW valid until Nov 2027',o:'g'},{t:'Greece flag missing — est. 5 days',o:'y'},{t:'Medical valid until Jul 2026',o:'g'},{t:'Available Jun 2026',o:'g'}]},
  { n:'Lazar Stoyanov',i:'LS',c:'#B71C1C',rank:'AB',nat:'Bulgarian',yrs:2,ai:68,cr:64,dr:32,s:'Not ready',sc:'r-red',
    checks:[{t:'COC valid until Jun 2028',o:'g'},{t:'STCW expired Dec 2025 — renewal 2 weeks',o:'r'},{t:'Panama flag expired — renewal needed',o:'y'},{t:'Medical expired Jan 2026 — PEME required',o:'r'},{t:'Available May 2026',o:'g'}]}
]
const matches = [
  {pct:94,cand:'Marco Villanueva',prin:'Varship Shipping',reason:'3rd Officer, tanker, Filipino, CrewScore 85, Panama flag',sc:'r-green'},
  {pct:89,cand:'Oleksandr Boyko',prin:'Diana Shipping',reason:'Chief Engineer, bulk, Ukrainian, CrewScore 82, 10+ yrs',sc:'r-green'},
  {pct:82,cand:'Rodel Santos',prin:'Capital Ship Mgmt',reason:'2nd Officer, container, Filipino, CrewScore 78',sc:'r-yellow'},
  {pct:76,cand:'Ion Popescu',prin:'Costamare Inc.',reason:'Bosun, container, Romanian, CrewScore 74',sc:'r-yellow'}
]
const alerts = [
  {type:'urgent',text:'Lazar Stoyanov — STCW expired Dec 2025, cannot deploy'},
  {type:'urgent',text:'Lazar Stoyanov — Medical expired Jan 2026'},
  {type:'warn',text:'Ruslan Goncharov — Greece flag endorsement missing'},
  {type:'warn',text:'Chen Wei — Medical due renewal in 45 days'},
  {type:'warn',text:'Paolo Castillo — COC expires in 4 months'},
  {type:'info',text:'DMW license renewal due in 180 days'},
  {type:'info',text:'3 new STCW 2026 guidelines published'}
]

const principals = [
  {name:'Varship Shipping Co.',vessels:[
    {name:'MT Varship Glory',imo:'9546239',type:'Oil/Chem Tanker',dwt:'33,500',flag:'Marshall Islands',crew:22,nats:4,salary:'$67,400',rel1:2,rel2:3,planned:4,
      ranks:[
        {r:'Master',bars:[{n:'Andreou N.',s:'2025-11-10',e:'2026-05-10',t:'expiring'},{n:'Petridis K.',s:'2026-05-15',e:'2026-11-15',t:'planned'}]},
        {r:'Chief Officer',bars:[{n:'Varias S.',s:'2026-01-05',e:'2026-07-05',t:'active'},{n:'Pop A.',s:'2026-07-10',e:'2027-01-10',t:'planned'}]},
        {r:'2nd Officer',bars:[{n:'Goncharov R.',s:'2026-02-01',e:'2026-08-01',t:'active'}]},
        {r:'3rd Officer',bars:[{n:'Dela Cruz J.',s:'2026-03-15',e:'2026-09-15',t:'active'}]},
        {r:'Chief Engineer',bars:[{n:'Petrov D.',s:'2025-10-01',e:'2026-04-15',t:'expired'},{n:'Patel R.',s:'2026-04-20',e:'2026-10-20',t:'active'}]},
        {r:'2nd Engineer',bars:[{n:'Santos M.',s:'2026-01-20',e:'2026-07-20',t:'active'}]},
        {r:'3rd Engineer',bars:[{n:'Castillo P.',s:'2026-02-10',e:'2026-06-10',t:'expiring'},{n:'Wei C.',s:'2026-06-15',e:'2026-12-15',t:'planned'}]},
        {r:'Bosun',bars:[{n:'Reyes M.',s:'2025-12-01',e:'2026-06-01',t:'expiring'},{n:'Hassan A.',s:'2026-06-05',e:'2026-12-05',t:'planned'}]},
        {r:'AB',bars:[{n:'Ionescu R.',s:'2026-01-15',e:'2026-07-15',t:'active'}]},
        {r:'Oiler',bars:[{n:'Reyes J.',s:'2025-11-20',e:'2026-05-20',t:'expiring'},{n:'TBD',s:'2026-05-25',e:'2026-11-25',t:'planned'}]},
        {r:'Cook',bars:[{n:'Santos R.',s:'2026-02-15',e:'2026-08-15',t:'active'}]}
      ]},
    {name:'MT Aegean Sun',imo:'9612345',type:'Oil Tanker',dwt:'47,200',flag:'Greece',crew:24,nats:3,salary:'$72,100',rel1:1,rel2:2,planned:3,
      ranks:[
        {r:'Master',bars:[{n:'Papadopoulos G.',s:'2026-01-10',e:'2026-07-10',t:'active'}]},
        {r:'Chief Officer',bars:[{n:'Marinov D.',s:'2025-12-05',e:'2026-06-05',t:'expiring'},{n:'Boyko O.',s:'2026-06-10',e:'2026-12-10',t:'planned'}]},
        {r:'2nd Officer',bars:[{n:'Reyes C.',s:'2026-02-20',e:'2026-08-20',t:'active'}]},
        {r:'Chief Engineer',bars:[{n:'Koval V.',s:'2026-01-15',e:'2026-07-15',t:'active'}]},
        {r:'Bosun',bars:[{n:'Ahmad H.',s:'2025-11-15',e:'2026-05-15',t:'expiring'},{n:'TBD',s:'2026-05-20',e:'2026-11-20',t:'planned'}]},
        {r:'AB',bars:[{n:'Cruz R.',s:'2026-03-10',e:'2026-09-10',t:'active'}]}
      ]},
    {name:'MT Varship Star',imo:'9678901',type:'Chemical Tanker',dwt:'19,800',flag:'Liberia',crew:20,nats:5,salary:'$58,300',rel1:0,rel2:1,planned:2,
      ranks:[
        {r:'Master',bars:[{n:'Nikos A.',s:'2026-02-01',e:'2026-08-01',t:'active'}]},
        {r:'Chief Officer',bars:[{n:'Petrov I.',s:'2026-01-20',e:'2026-07-20',t:'active'}]},
        {r:'Chief Engineer',bars:[{n:'Kim S.',s:'2026-01-10',e:'2026-07-10',t:'active'},{n:'Lee J.',s:'2026-07-15',e:'2027-01-15',t:'planned'}]},
        {r:'Bosun',bars:[{n:'Santos L.',s:'2026-02-10',e:'2026-08-10',t:'active'}]}
      ]}
  ]},
  {name:'Diana Shipping',vessels:[
    {name:'MV Diana Explorer',imo:'9456789',type:'Bulk Carrier',dwt:'82,000',flag:'Marshall Islands',crew:23,nats:4,salary:'$63,200',rel1:1,rel2:2,planned:3,
      ranks:[
        {r:'Master',bars:[{n:'Smith J.',s:'2026-01-15',e:'2026-07-15',t:'active'}]},
        {r:'Chief Officer',bars:[{n:'Andreou P.',s:'2025-12-01',e:'2026-06-01',t:'expiring'},{n:'TBD',s:'2026-06-05',e:'2026-12-05',t:'planned'}]},
        {r:'Chief Engineer',bars:[{n:'Petrov D.',s:'2026-02-01',e:'2026-08-01',t:'active'}]},
        {r:'Bosun',bars:[{n:'Reyes E.',s:'2026-01-05',e:'2026-07-05',t:'active'}]},
        {r:'AB',bars:[{n:'Cruz M.',s:'2025-11-20',e:'2026-05-20',t:'expiring'},{n:'Marinescu A.',s:'2026-05-25',e:'2026-11-25',t:'planned'}]}
      ]},
    {name:'MV Parthenon',imo:'9567890',type:'Bulk Carrier',dwt:'76,500',flag:'Greece',crew:22,nats:3,salary:'$61,800',rel1:0,rel2:1,planned:2,
      ranks:[
        {r:'Master',bars:[{n:'Georgiou N.',s:'2026-02-10',e:'2026-08-10',t:'active'}]},
        {r:'Chief Officer',bars:[{n:'Ivanov S.',s:'2026-01-25',e:'2026-07-25',t:'active'}]},
        {r:'Chief Engineer',bars:[{n:'Wei L.',s:'2026-01-05',e:'2026-07-05',t:'active'},{n:'TBD',s:'2026-07-10',e:'2027-01-10',t:'planned'}]},
        {r:'Bosun',bars:[{n:'Kolev B.',s:'2026-02-20',e:'2026-08-20',t:'active'}]}
      ]}
  ]},
  {name:'Capital Ship Mgmt',vessels:[
    {name:'MV Capital Fortune',imo:'9345678',type:'Container',dwt:'65,000',flag:'Marshall Islands',crew:24,nats:5,salary:'$70,400',rel1:1,rel2:3,planned:4,
      ranks:[
        {r:'Master',bars:[{n:'Park S.',s:'2026-01-10',e:'2026-07-10',t:'active'}]},
        {r:'Chief Officer',bars:[{n:'Dela Cruz A.',s:'2025-11-15',e:'2026-05-15',t:'expiring'},{n:'Santos R.',s:'2026-05-20',e:'2026-11-20',t:'planned'}]},
        {r:'Chief Engineer',bars:[{n:'Patel V.',s:'2026-02-05',e:'2026-08-05',t:'active'}]},
        {r:'AB',bars:[{n:'Reyes T.',s:'2026-01-20',e:'2026-07-20',t:'active'}]}
      ]}
  ]},
  {name:'Costamare Inc.',vessels:[
    {name:'MV Cosco Hellas',imo:'9234567',type:'Container',dwt:'54,000',flag:'Greece',crew:21,nats:3,salary:'$59,800',rel1:2,rel2:2,planned:2,
      ranks:[
        {r:'Master',bars:[{n:'Papadakis E.',s:'2025-12-10',e:'2026-06-10',t:'expiring'},{n:'TBD',s:'2026-06-15',e:'2026-12-15',t:'planned'}]},
        {r:'Chief Officer',bars:[{n:'Reyes F.',s:'2026-01-20',e:'2026-07-20',t:'active'}]},
        {r:'Chief Engineer',bars:[{n:'Stoyanov P.',s:'2025-11-05',e:'2026-05-05',t:'expiring'},{n:'Wei K.',s:'2026-05-10',e:'2026-11-10',t:'planned'}]},
        {r:'Bosun',bars:[{n:'Popescu R.',s:'2026-02-15',e:'2026-08-15',t:'active'}]}
      ]}
  ]}
]

const currentPrincipal = computed(() => principals[selPrincipal.value])
const currentVessel = computed(() => {
  const p = currentPrincipal.value
  if (selVessel.value >= p.vessels.length) selVessel.value = 0
  return p.vessels[selVessel.value]
})

function onPrincipalChange() { selVessel.value = 0 }

const SD = new Date(2026,3,1), ED = new Date(2026,9,1)
const totalDays = (ED - SD) / 864e5
function pct(d) { const x = (new Date(d) - SD) / 864e5; return Math.max(0, Math.min(100, x / totalDays * 100)) }
const todayPct = pct('2026-04-06')

function barStyle(b) {
  const l = pct(b.s), w = pct(b.e) - l
  return w > 0 ? { left: l + '%', width: w + '%' } : { display: 'none' }
}
function daysClass(d){return d<=30?'days-u':d<=60?'days-w':'days-ok'}

const aiSubTab = ref(0)
const aiSubTabs = [
  {name:'Cost calculator', tip:'Αυτόματος υπολογισμός κόστους deployment ανά υποψήφιο — ιατρικά, ταξίδι, σημαία, βίζα'},
  {name:'Compare candidates', tip:'Σύγκριση υποψηφίων πλάι-πλάι σε 13 κριτήρια — AI score, CrewScore, κόστος, χρόνος'},
  {name:'Deploy timeline', tip:'Βήμα-βήμα χρονοδιάγραμμα deployment — από shortlist μέχρι επιβίβαση'},
  {name:'Principal intel', tip:'Τι προτιμάει κάθε πλοιοκτήτης — εθνικότητα, εμπειρία, CrewScore, ιστορικό τοποθετήσεων'},
  {name:'Auto-checklist', tip:'Αυτόματη λίστα ελέγχου 9 βημάτων — τι ολοκληρώθηκε, τι εκκρεμεί, τι περιμένει'}
]
const selectedDeployCandidate = ref(0)
const deployCandidates = [
  { name:'Juan Dela Cruz', i:'JD', color:'#0A66C2', nat:'Filipino', origin:'Manila, Philippines', dest:'Piraeus, Greece', rank:'3rd Officer',
    costs:{medical:350,travel:890,flag:0,visa:200,dmw:150,insurance:250,agency:400,renewals:0},
    notes:'All documents valid. No renewals needed. Direct deployment.' },
  { name:'Ruslan Goncharov', i:'RG', color:'#E7A33E', nat:'Ukrainian', origin:'Odessa, Ukraine', dest:'Piraeus, Greece', rank:'2nd Officer',
    costs:{medical:280,travel:420,flag:160,visa:150,dmw:0,insurance:250,agency:400,renewals:190},
    notes:'Greece flag endorsement missing — est. $160 + 5 days processing. Higher total due to renewals.' },
  { name:'Lazar Stoyanov', i:'LS', color:'#B71C1C', nat:'Bulgarian', origin:'Varna, Bulgaria', dest:'Piraeus, Greece', rank:'AB',
    costs:{medical:400,travel:280,flag:160,visa:0,insurance:250,agency:400,dmw:0,renewals:710},
    notes:'STCW renewal $350 + Medical PEME $400 (expired). Panama flag $160. High risk — 21 day timeline.' }
]
const selectedCand = computed(() => deployCandidates[selectedDeployCandidate.value] || null)
const candTotal = computed(() => selectedCand.value ? Object.values(selectedCand.value.costs).reduce((s,v) => s + v, 0) : 0)

const allCompareData = [
  // Request 0: Varship 3rd Officer
  [
    {label:'AI score',v:['92','87','68'],best:0},
    {label:'CrewScore',v:['87','82','64'],best:0},
    {label:'DR score',v:['96','68','32'],best:0},
    {label:'Sea service',v:['4 yrs','6 yrs','2 yrs'],best:1},
    {label:'Nationality',v:['Filipino','Ukrainian','Bulgarian'],best:-1},
    {label:'COC',v:['Valid 2030','Valid 2029','Valid 2028'],best:0},
    {label:'STCW',v:['Valid 2028','Valid 2027','Expired!'],best:0,bad:2},
    {label:'Flag endorse.',v:['Panama active','Greece missing','Panama expired'],best:0,warn:1,bad:2},
    {label:'Medical',v:['Valid Sep 2026','Valid Jul 2026','Expired!'],best:0,bad:2},
    {label:'Availability',v:['May 2026','Jun 2026','May 2026'],best:0},
    {label:'Est. cost',v:['$2,240','$1,850','$2,200'],best:1},
    {label:'Est. timeline',v:['5 days','12 days','21 days'],best:0,warn:1,bad:2},
    {label:'AI verdict',v:['Best match','Needs flag','Not recommended'],best:0,warn:1,bad:2}
  ],
  // Request 1: Diana Chief Engineer
  [
    {label:'AI score',v:['95','86','89','74'],best:0},
    {label:'CrewScore',v:['91','83','84','69'],best:0},
    {label:'DR score',v:['94','78','92','52'],best:0},
    {label:'Sea service',v:['12 yrs','14 yrs','7 yrs','4 yrs'],best:1},
    {label:'Nationality',v:['Russian','Indian','Filipino','Chinese'],best:-1},
    {label:'COC',v:['Valid 2029','Valid 2028','Valid 2030','Valid 2027'],best:2},
    {label:'STCW',v:['Valid 2028','Valid 2027','Valid 2029','Expiring'],best:2,warn:3},
    {label:'Flag endorse.',v:['Marshall Is. active','Needs Liberia','Marshall Is. active','Needs flag'],best:0,warn:1,bad:3},
    {label:'Medical',v:['Valid Aug 2026','Valid Jun 2026','Valid Oct 2026','Due in 45d'],best:2,warn:3},
    {label:'Availability',v:['Apr 2026','May 2026','May 2026','Jun 2026'],best:0},
    {label:'Est. cost',v:['$1,680','$1,980','$2,240','$2,450'],best:0},
    {label:'Est. timeline',v:['4 days','7 days','5 days','14 days'],best:0,warn:3,bad:3},
    {label:'AI verdict',v:['Best match','Strong option','Good backup','Needs renewals'],best:0,warn:3}
  ]
]
const compareData = computed(() => allCompareData[selectedRequest.value] || allCompareData[0])
const compareCandidates = computed(() => deployCandidates.value || [])

const allDeploySteps = [
  // Request 0: Varship
  [
    {title:'Shortlist received from Varship',sub:'Apr 5, 2026',day:'Day 0',status:'done'},
    {title:'Candidate vetting & verification',sub:'Apr 6 — All docs verified, CrewScore 87',day:'Day 1',status:'done'},
    {title:'Medical examination (PEME)',sub:'Apr 7-8 — Manila Doctors Hospital, $350',day:'Day 2-3',status:'prog'},
    {title:'Contract signing & DMW processing',sub:'Apr 9 — OEC issuance, insurance, bonds',day:'Day 4',status:'pend'},
    {title:'Travel booking & departure',sub:'Apr 10 — Manila → Piraeus via Doha, $890',day:'Day 5',status:'pend'},
    {title:'Arrival & embarkation',sub:'Apr 11 — Join MT Varship Glory at Piraeus',day:'Day 6',status:'pend'}
  ],
  // Request 1: Diana
  [
    {title:'Shortlist received from Diana Shipping',sub:'Apr 4, 2026',day:'Day 0',status:'done'},
    {title:'Candidate vetting & verification',sub:'Apr 5 — All docs verified, CrewScore 91',day:'Day 1',status:'done'},
    {title:'Medical examination (PEME)',sub:'Apr 6 — St Petersburg clinic, $300',day:'Day 2',status:'done'},
    {title:'Contract signing & processing',sub:'Apr 7 — Insurance, bonds, travel docs',day:'Day 3',status:'prog'},
    {title:'Travel booking & departure',sub:'Apr 8 — St Petersburg → Piraeus, $520',day:'Day 4',status:'pend'},
    {title:'Arrival & embarkation',sub:'Apr 9 — Join MV Diana Explorer at Piraeus',day:'Day 5',status:'pend'}
  ]
]
const deploySteps = computed(() => allDeploySteps[selectedRequest.value] || allDeploySteps[0])
const deployKpis = computed(() => {
  const r = selectedRequest.value
  return r === 0
    ? {days:'5 days',cost:'$2,240',dr:'96',target:'May 15',cand:'Juan Dela Cruz',vessel:'MT Varship Glory'}
    : {days:'4 days',cost:'$1,680',dr:'94',target:'Jun 1',cand:'Dmitry Petrov',vessel:'MV Diana Explorer'}
})

const allPrincipalIntel = [
  // Request 0: Varship
  {
    name:'Varship Shipping Co.',
    prefs:[
      {k:'Preferred nationality',v:'Filipino (78% of hires)'},
      {k:'Min experience',v:'4+ years sea service'},
      {k:'Min CrewScore',v:'80+'},
      {k:'Vessel focus',v:'Oil/Chemical Tankers'},
      {k:'Avg response time',v:'1.2 days'},
      {k:'Acceptance rate',v:'96%'},
      {k:'Total placements',v:'142 (since 2024)'},
      {k:'Avg contract',v:'6.2 months'}
    ],
    history:[
      {date:'Mar 2026',role:'Chief Engineer → MT Aegean Sun',who:'Raj Patel (Indian, 14 yrs)',cost:'$3,100',days:'7d'},
      {date:'Feb 2026',role:'AB → MT Varship Star',who:'Ionescu R. (Romanian, 5 yrs)',cost:'$1,800',days:'4d'},
      {date:'Jan 2026',role:'Chief Officer → MT Varship Glory',who:'Varias S. (Greek, 12 yrs)',cost:'$2,200',days:'6d'},
      {date:'Dec 2025',role:'Bosun → MT Aegean Sun',who:'Ahmad H. (Indonesian, 8 yrs)',cost:'$1,600',days:'5d'}
    ]
  },
  // Request 1: Diana
  {
    name:'Diana Shipping',
    prefs:[
      {k:'Preferred nationality',v:'EU officers (65% of hires)'},
      {k:'Min experience',v:'8+ years sea service'},
      {k:'Min CrewScore',v:'75+'},
      {k:'Vessel focus',v:'Bulk Carriers'},
      {k:'Avg response time',v:'1.8 days'},
      {k:'Acceptance rate',v:'91%'},
      {k:'Total placements',v:'98 (since 2024)'},
      {k:'Avg contract',v:'7.1 months'}
    ],
    history:[
      {date:'Mar 2026',role:'2nd Officer → MV Parthenon',who:'Santos J. (Filipino, 6 yrs)',cost:'$2,400',days:'6d'},
      {date:'Feb 2026',role:'Bosun → MV Diana Explorer',who:'Kolev B. (Bulgarian, 8 yrs)',cost:'$1,500',days:'4d'},
      {date:'Jan 2026',role:'Chief Officer → MV Parthenon',who:'Ivanov S. (Russian, 10 yrs)',cost:'$1,900',days:'5d'},
      {date:'Nov 2025',role:'3rd Engineer → MV Diana Explorer',who:'Wei L. (Chinese, 5 yrs)',cost:'$2,100',days:'8d'}
    ]
  }
]
const principalIntel = computed(() => allPrincipalIntel[selectedRequest.value] || allPrincipalIntel[0])

const allChecklists = [
  // Request 0: Varship - Juan Dela Cruz
  [
    {text:'Shortlist received & accepted',s:'done'},
    {text:'Candidate identity verified',s:'done'},
    {text:'COC verified (valid Dec 2030)',s:'done'},
    {text:'STCW verified (valid Aug 2028)',s:'done'},
    {text:'Flag endorsement confirmed (Panama)',s:'done'},
    {text:'Medical examination (PEME)',s:'prog',note:'Scheduled Apr 7'},
    {text:'Contract signing & DMW processing',s:'prog',note:'Pending medical'},
    {text:'Travel booking (Manila → Piraeus)',s:'pend'},
    {text:'Embarkation confirmation',s:'pend'}
  ],
  // Request 1: Diana - Dmitry Petrov
  [
    {text:'Shortlist received & accepted',s:'done'},
    {text:'Candidate identity verified',s:'done'},
    {text:'COC verified (valid Mar 2029)',s:'done'},
    {text:'STCW verified (valid Nov 2028)',s:'done'},
    {text:'Flag endorsement confirmed (Marshall Islands)',s:'done'},
    {text:'Medical examination (PEME)',s:'done',note:'Completed Apr 6'},
    {text:'Contract signing & processing',s:'prog',note:'In progress'},
    {text:'Travel booking (St Petersburg → Piraeus)',s:'pend'},
    {text:'Embarkation confirmation',s:'pend'}
  ]
]
const checklist = computed(() => allChecklists[selectedRequest.value] || allChecklists[0])
const checkDone = computed(() => checklist.value.filter(c => c.s === 'done').length)
const checkProg = computed(() => checklist.value.filter(c => c.s === 'prog').length)
const checkPend = computed(() => checklist.value.filter(c => c.s === 'pend').length)
const checkPct = computed(() => Math.round(checkDone.value / checklist.value.length * 100))

</script>
<template>
<div class="container ad">
  <div class="ad-hdr"><div class="ad-hdr-l"><div class="avatar avatar-lg avatar-agent">MA</div><div><h1>Magsaysay Maritime Corp.</h1><p class="ad-sub">AI-powered deployment management · DMW-1234-2026</p></div></div></div>
  <div class="kg"><div class="kc" title="Αιτήματα πλοιοκτητών που περιμένουν απάντηση"><div class="kl">Incoming requests</div><div class="kv" style="color:#B71C1C">4</div><div class="kt">2 urgent</div></div><div class="kc" title="Ναυτικοί σε διαδικασία αξιολόγησης και deployment"><div class="kl">In pipeline</div><div class="kv" style="color:#0A66C2">18</div><div class="kt ku">+5 this week</div></div><div class="kc" title="Ναυτικοί με όλα τα έγγραφα έτοιμα για αναχώρηση"><div class="kl">Ready to deploy</div><div class="kv" style="color:#1B5E20">6</div><div class="kt ku">All verified</div></div><div class="kc" title="Μέσος χρόνος από αίτημα μέχρι επιβίβαση στο πλοίο"><div class="kl">Avg deploy time</div><div class="kv">8.4 days</div><div class="kt ku">-2.1 vs last month</div></div><div class="kc" title="Ποσοστό επιτυχημένων τοποθετήσεων φέτος"><div class="kl">Placement rate</div><div class="kv" style="color:#1B5E20">94%</div><div class="kt ku">+3% vs Q1</div></div><div class="kc" title="Αριθμός πλοιοκτητών που εξυπηρετείτε"><div class="kl">Principals</div><div class="kv" style="color:#0A66C2">8</div><div class="kt">All DMW-verified</div></div><div class="kc" title="Συνολικές τοποθετήσεις από αρχή του έτους"><div class="kl">Deployments YTD</div><div class="kv" style="color:#1D9E75">487</div><div class="kt ku">+42 this month</div></div><div class="kc" title="Μέση βαθμολογία CrewScore των ναυτικών που τοποθετήσατε"><div class="kl">Avg CrewScore placed</div><div class="kv" style="color:#1D9E75">81</div><div class="kt ku">Above avg</div></div></div>
  <div class="dtabs"><button v-for="(t,i) in tabs" :key="i" class="dtab" :class="{active:activeTab===i}" @click="activeTab=i" :title="t.tip">{{t.name}}</button></div>

  <!-- Tab 0: Shortlist Analyzer -->
  <div v-if="activeTab===3">
    <div v-for="sl in shortlists" :key="sl.owner" class="card sec sl-card">
      <div class="sl-top"><div class="sl-ow"><div class="sl-av" :style="{background:sl.color}">{{sl.i}}</div><div><strong>{{sl.owner}}</strong><span class="sl-tm">{{sl.time}} · {{sl.urgency}}</span></div></div><span class="rd" :class="sl.readyPct>=80?'rg':sl.readyPct>=50?'ry':'rr'">{{sl.readyPct}}% ready</span></div>
      <div class="sl-pos">{{sl.position}}</div>
      <div class="sl-bar"><div class="sl-fill" :style="{width:sl.readyPct+'%',background:sl.readyPct>=80?'#1B5E20':'#E7A33E'}"></div></div>
      <div class="sl-cands"><div v-for="c in sl.candidates" :key="c.i" class="sl-c"><div class="sl-ca" :style="{background:c.c}">{{c.i}}</div>{{c.n}}<span class="rd" :class="c.sc">{{c.s}}</span></div></div>
      <div class="sl-acts"><button class="btn btn-primary btn-sm">Review candidates</button><button class="btn btn-secondary btn-sm">Message owner</button></div>
    </div>
  </div>

  <!-- Tab 1: Deployment Readiness -->
  <div v-if="activeTab===4">
    <div class="card sec">
      <div class="dr-leg"><span><span class="dot dg"></span> Ready</span><span><span class="dot dy"></span> Needs action</span><span><span class="dot dr"></span> Not ready</span><span class="dr-r">AI · Crew · DR scores</span></div>
      <div v-for="(c,ci) in readiness" :key="c.i" class="dr-cand" @click="expandedCandidate=expandedCandidate===ci?null:ci">
        <div class="dr-row"><div class="dr-av" :style="{background:c.c}">{{c.i}}</div><div class="dr-inf"><strong>{{c.n}}</strong><span>{{c.rank}} · {{c.nat}} · {{c.yrs}} yrs</span></div><div class="dr-scs"><div class="dsc sai">{{c.ai}}</div><div class="dsc scr">{{c.cr}}</div><div class="dsc sdr">{{c.dr}}</div></div><span class="rd" :class="c.sc">{{c.s}}</span></div>
        <div v-if="expandedCandidate===ci" class="dr-cks"><div v-for="ch in c.checks" :key="ch.t" class="dr-ck" :class="'ck-'+ch.o"><span class="dot" :class="'d'+ch.o"></span>{{ch.t}}</div></div>
      </div>
    </div>
  </div>

  <!-- Tab 2: Principal Matching -->
  <div v-if="activeTab===2">
    <div class="card sec">
      <h2>AI principal matching</h2><p class="sec-sub">AI analyzes hiring history and preferences to suggest best matches.</p>
      <div v-for="m in matches" :key="m.cand" class="pm-row"><div class="pm-pct">{{m.pct}}%</div><div class="pm-inf"><strong>{{m.cand}} → {{m.prin}}</strong><span>{{m.reason}}</span></div><span class="rd" :class="m.sc">{{m.pct>=85?'Auto-suggest':'Review'}}</span></div>
    </div>
  </div>

  <!-- Tab 3: Crew Calendar with Gantt -->
  <div v-if="activeTab===0">
    <div class="card sec">
      <div class="gantt-controls">
        <div class="gc-item">
          <label>Principal</label>
          <select v-model="selPrincipal" @change="onPrincipalChange" class="gc-select">
            <option v-for="(p,i) in principals" :key="i" :value="i">{{p.name}} ({{p.vessels.length}} vessels)</option>
          </select>
        </div>
        <div class="gc-item">
          <label>Vessel</label>
          <select v-model="selVessel" class="gc-select">
            <option v-for="(v,i) in currentPrincipal.vessels" :key="i" :value="i">{{v.name}} ({{v.type}})</option>
          </select>
        </div>
      </div>

      <!-- Vessel Header -->
      <div class="v-header">
        <div class="v-icon-w"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M2 20l2-2 2 2 2-2 2 2 2-2 2 2 2-2 2 2 2-2 2 2"/><path d="M4 18V8a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v10"/><path d="M12 6V3"/></svg></div>
        <div><strong>{{currentVessel.name}}</strong><div class="v-meta">IMO {{currentVessel.imo}} | {{currentVessel.type}} | {{currentVessel.dwt}} DWT | {{currentVessel.flag}}</div></div>
      </div>

      <!-- Vessel KPIs -->
      <div class="v-kpis">
        <div class="vk"><div class="vk-v vk-b">{{currentVessel.crew}}</div><div class="vk-l">Crew on board</div></div>
        <div class="vk"><div class="vk-v">{{currentVessel.nats}}</div><div class="vk-l">Nationalities</div></div>
        <div class="vk"><div class="vk-v vk-b">{{currentVessel.salary}}</div><div class="vk-l">Monthly salaries</div></div>
        <div class="vk"><div class="vk-v vk-d">{{currentVessel.rel1}}</div><div class="vk-l">Relief &lt;1 month</div></div>
        <div class="vk"><div class="vk-v vk-w">{{currentVessel.rel2}}</div><div class="vk-l">Relief &lt;2 months</div></div>
        <div class="vk"><div class="vk-v vk-g">{{currentVessel.planned}}</div><div class="vk-l">Planned lineups</div></div>
      </div>

      <!-- Gantt Chart -->
      <div class="gantt">
        <div class="g-hdr"><span>Position</span><span>Apr 26</span><span>May 26</span><span>Jun 26</span><span>Jul 26</span><span>Aug 26</span><span>Sep 26</span></div>
        <div v-for="rk in currentVessel.ranks" :key="rk.r" class="g-row">
          <div class="g-rank">{{rk.r}}</div>
          <div class="g-bars">
            <div class="g-today" :style="{left: todayPct+'%'}"></div>
            <div v-for="b in rk.bars" :key="b.n" class="g-bar" :class="'b-'+b.t" :style="barStyle(b)" :title="b.n+' | '+b.s+' → '+b.e">{{b.n}}</div>
          </div>
        </div>
      </div>

      <!-- Legend -->
      <div class="gantt-legend">
        <span><span class="gl-dot" style="background:#0A66C2"></span> Active</span>
        <span><span class="gl-dot" style="background:#E7A33E"></span> Expiring</span>
        <span><span class="gl-dot" style="background:#E24B4A"></span> Expired</span>
        <span><span class="gl-dot" style="background:#1D9E75;border:1px dashed rgba(0,0,0,0.3)"></span> Planned</span>
        <span><span class="gl-dot" style="background:#E24B4A;width:2px;height:14px;border-radius:0"></span> Today</span>
      </div>
    </div>

    <!-- Cost + Summary row -->
    <div class="tcol">
      <div class="card sec"><h3>Cost breakdown (typical)</h3><div class="cr"><span>Medical (PEME)</span><strong>$350</strong></div><div class="cr"><span>Travel Manila→Piraeus</span><strong>$890</strong></div><div class="cr"><span>Flag endorsement</span><strong>$160</strong></div><div class="cr"><span>Visa processing</span><strong>$200</strong></div><div class="cr"><span>DMW/OEC processing</span><strong>$150</strong></div><div class="cr"><span>Insurance</span><strong>$250</strong></div><div class="cr"><span>Agency fee</span><strong>$400</strong></div><div class="cr crt"><span>Estimated total</span><strong>$2,400</strong></div></div>
      <div class="card sec"><h3>Calendar summary</h3><div class="cr"><span>Next 30 days</span><strong style="color:#B71C1C">2 changes</strong></div><div class="cr"><span>30-60 days</span><strong style="color:#E65100">3 changes</strong></div><div class="cr"><span>60-90 days</span><strong>1 change</strong></div><div class="cr crt"><span>Pipeline cost</span><strong style="color:#0A66C2;font-size:18px">$13,700</strong></div></div>
    </div>
  </div>


  <!-- Tab 4: AI Intelligence -->
  <div v-if="activeTab===1">
    <div class="ai-subtabs">
      <button v-for="(st,si) in aiSubTabs" :key="si" class="ai-stab" :class="{active:aiSubTab===si}" @click="aiSubTab=si" :title="st.tip">{{st.name}}</button>
    </div>

    <!-- AI Cost Calculator -->
    <div v-if="aiSubTab===0">
      <div class="card sec">
        <h3>Select candidate for cost analysis</h3>
        <div class="cand-select">
          <div v-for="(dc,di) in deployCandidates" :key="di" class="cand-opt" :class="{active:selectedDeployCandidate===di}" @click="selectedDeployCandidate=di">
            <div class="cand-av" :style="{background:dc.color}">{{dc.i}}</div>
            <div class="cand-opt-info"><strong>{{dc.name}}</strong><span>{{dc.nat}} · {{dc.rank}}</span></div>
          </div>
        </div>
      </div>

      <div class="tcol">
        <div class="card sec">
          <h3>AI cost breakdown — {{selectedCand.name}}</h3>
          <div class="route-info">
            <div class="route-from"><span class="route-dot" style="background:#0A66C2"></span>{{selectedCand.origin}}</div>
            <div class="route-arrow">→</div>
            <div class="route-to"><span class="route-dot" style="background:#1B5E20"></span>{{selectedCand.dest}}</div>
          </div>
          <div class="cost-breakdown">
            <div class="cb-row" v-if="selectedCand.costs.medical"><span>Medical (PEME)</span><strong>${{selectedCand.costs.medical}}</strong></div>
            <div class="cb-row" v-if="selectedCand.costs.travel"><span>Travel ({{selectedCand.origin.split(',')[0]}} → {{selectedCand.dest.split(',')[0]}})</span><strong>${{selectedCand.costs.travel}}</strong></div>
            <div class="cb-row" v-if="selectedCand.costs.flag"><span>Flag endorsement</span><strong>${{selectedCand.costs.flag}}</strong></div>
            <div class="cb-row" v-if="selectedCand.costs.visa"><span>Visa processing</span><strong>${{selectedCand.costs.visa}}</strong></div>
            <div class="cb-row" v-if="selectedCand.costs.dmw"><span>DMW/OEC processing</span><strong>${{selectedCand.costs.dmw}}</strong></div>
            <div class="cb-row"><span>Insurance & bonds</span><strong>${{selectedCand.costs.insurance}}</strong></div>
            <div class="cb-row"><span>Agency processing fee</span><strong>${{selectedCand.costs.agency}}</strong></div>
            <div class="cb-row cb-warn" v-if="selectedCand.costs.renewals"><span>Certificate renewals</span><strong>${{selectedCand.costs.renewals}}</strong></div>
            <div class="cb-row cb-total"><span>Total deployment cost</span><strong>${{candTotal.toLocaleString()}}</strong></div>
          </div>
          <div class="ai-note">{{selectedCand.notes}}</div>
        </div>
        <div>
          <div class="card sec">
            <h3>Cost comparison — all candidates</h3>
            <div class="cost-compare">
              <div v-for="(dc,di) in deployCandidates" :key="di" class="cc-card" :class="{'cc-best':di===0}">
                <div class="cc-val" :style="{color:dc.color}">${{Object.values(dc.costs).reduce((s,v)=>s+v,0).toLocaleString()}}</div>
                <div class="cc-name">{{dc.name}}</div>
                <div class="cc-route">{{dc.origin.split(',')[0]}} → {{dc.dest.split(',')[0]}}</div>
                <span v-if="di===0" class="rd rg">AI recommended</span>
                <span v-else-if="di===1" class="rd ry">Needs flag</span>
                <span v-else class="rd rr">High risk</span>
              </div>
            </div>
          </div>
          <div class="card sec">
            <h3>Quarterly performance</h3>
            <div class="savings-bar"><div class="savings-fill" style="width:72%">$18,400 saved (72%)</div></div>
            <div class="cr"><span>Budget Q2</span><strong>$25,600</strong></div>
            <div class="cr"><span>Actual spent</span><strong style="color:#1B5E20">$7,200</strong></div>
            <div class="cr"><span>Avg cost/deploy</span><strong>$2,340</strong></div>
            <div class="cr"><span>Industry avg</span><strong style="color:var(--color-text-tertiary)">$3,100</strong></div>
          </div>
        </div>
      </div>
    </div>

    <!-- Candidate Compare -->
    <div v-if="aiSubTab===1">
      <div class="card sec">
        <h3>Side-by-side — {{shortlists[selectedRequest]?.position || 'Candidates'}}</h3>
        <div class="cmp-grid">
          <div class="cmp-hdr">Criteria</div><div v-for="dc in compareCandidates" :key="dc.i" class="cmp-hdr"><div class="av-sm" :style="{background:dc.color}">{{dc.i}}</div> {{dc.name}}</div>
          
          
          
          <template v-for="(row,ri) in compareData" :key="ri">
            <div class="cmp-label">{{row.label}}</div>
            <div v-for="(v,vi) in row.v" :key="vi" class="cmp-cell" :class="{'cmp-best':row.best===vi,'cmp-warn':row.warn===vi,'cmp-bad':row.bad===vi}">{{v}}</div>
          </template>
        </div>
      </div>
    </div>

    <!-- Deploy Timeline -->
    <div v-if="aiSubTab===2">
      <div class="card sec">
        <h3>Deployment timeline — {{deployKpis.cand}} → {{deployKpis.vessel}}</h3>
        <div class="tl-kpis">
          <div class="mc"><div class="mc-v" style="color:#1B5E20">{{deployKpis.days}}</div><div class="mc-l">Est. total</div></div>
          <div class="mc"><div class="mc-v" style="color:#0A66C2">{{deployKpis.cost}}</div><div class="mc-l">Est. cost</div></div>
          <div class="mc"><div class="mc-v" style="color:#1B5E20">{{deployKpis.dr}}</div><div class="mc-l">DR score</div></div>
          <div class="mc"><div class="mc-v">{{deployKpis.target}}</div><div class="mc-l">Target date</div></div>
        </div>
        <div class="tl-steps">
          <div v-for="(st,si) in deploySteps" :key="si">
            <div class="tl-step">
              <div class="tl-dot" :class="'tld-'+st.status"></div>
              <div class="tl-info"><strong>{{st.title}}</strong><span>{{st.sub}}</span></div>
              <div class="tl-day" :class="'tlt-'+st.status">{{st.day}}</div>
            </div>
            <div v-if="si<deploySteps.length-1" class="tl-line-v"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- Principal Intel -->
    <div v-if="aiSubTab===3">
      <div class="tcol">
        <div class="card sec">
          <h3>{{principalIntel.name}} — AI learned preferences</h3>
          <div v-for="p in principalIntel.prefs" :key="p.k" class="cr"><span>{{p.k}}</span><strong>{{p.v}}</strong></div>
        </div>
        <div class="card sec">
          <h3>Placement history — {{principalIntel.name}}</h3>
          <div v-for="h in principalIntel.history" :key="h.date+h.role" class="hist-row">
            <span class="hist-date">{{h.date}}</span>
            <div class="hist-info"><strong>{{h.role}}</strong><span>{{h.who}}</span></div>
            <span class="hist-cost">{{h.cost}}</span>
            <span class="hist-days">{{h.days}}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Auto Checklist -->
    <div v-if="aiSubTab===4">
      <div class="card sec">
        <h3>Deployment checklist — {{deployKpis.cand}} → {{deployKpis.vessel}}</h3>
        <div class="cl-kpis">
          <div class="mc"><div class="mc-v" style="color:#1B5E20">{{checkDone}}/{{checklist.length}}</div><div class="mc-l">Completed</div></div>
          <div class="mc"><div class="mc-v" style="color:#E7A33E">{{checkProg}}</div><div class="mc-l">In progress</div></div>
          <div class="mc"><div class="mc-v">{{checkPend}}</div><div class="mc-l">Pending</div></div>
          <div class="mc"><div class="mc-v" style="color:#1B5E20">{{checkPct}}%</div><div class="mc-l">Progress</div></div>
        </div>
        <div class="cl-list">
          <div v-for="cl in checklist" :key="cl.text" class="cl-item">
            <div class="cl-icon" :class="'cli-'+cl.s">{{cl.s==='done'?'\u2713':cl.s==='prog'?'!':'-'}}</div>
            <strong :class="{'cl-grey':cl.s==='pend'}">{{cl.text}}</strong>
            <span v-if="cl.note" class="cl-note">{{cl.note}}</span>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Tab 5: Compliance -->
  <div v-if="activeTab===5">
    <div class="tcol">
      <div class="card sec"><h2>Compliance alerts</h2><div v-for="a in alerts" :key="a.text" class="al-item"><span class="al-badge" :class="'al-'+a.type">{{a.type==='urgent'?'Urgent':a.type==='warn'?'Warning':'Info'}}</span><p>{{a.text}}</p></div></div>
      <div class="card sec"><h2>Pipeline performance</h2><div class="cr"><span>Avg received → vetting</span><strong>1.2 days</strong></div><div class="cr"><span>Avg vetting → contract</span><strong>3.8 days</strong></div><div class="cr"><span>Avg contract → deploy</span><strong>3.4 days</strong></div><div class="cr"><span>Total avg cycle</span><strong style="color:#1B5E20">8.4 days</strong></div><div class="cr"><span>Success rate</span><strong style="color:#1B5E20">94%</strong></div><div class="cr"><span>Deployments YTD</span><strong>487</strong></div><div class="cr"><span>Bottleneck</span><strong style="color:#E65100">Medical (5.2 days)</strong></div></div>
    </div>
  </div>
</div>
</template>
<style scoped>
.ad{padding-top:var(--space-5);padding-bottom:var(--space-8)}
.ad-hdr{display:flex;align-items:center;margin-bottom:var(--space-4)}.ad-hdr-l{display:flex;align-items:center;gap:var(--space-3)}.ad-hdr h1{font:var(--font-h1)}.ad-sub{font:var(--font-small);color:var(--color-text-secondary)}
.kg{display:grid;grid-template-columns:repeat(4,1fr);gap:var(--space-3);margin-bottom:var(--space-4)}.kc{background:var(--color-white);border:1px solid var(--color-border);border-radius:var(--radius-lg);padding:var(--space-4);text-align:center;box-shadow:0 2px 8px rgba(0,0,0,.06)}.kl{font:var(--font-small);color:var(--color-text-secondary);margin-bottom:6px}.kv{font-size:26px;font-weight:600}.kt{font:var(--font-caption);color:var(--color-text-tertiary);margin-top:4px}.ku{color:var(--color-success)}
.dtabs{display:flex;border-bottom:2px solid var(--color-border);margin-bottom:var(--space-4)}.dtab{padding:var(--space-3) var(--space-4);background:none;border:none;font:var(--font-small);font-weight:500;color:var(--color-text-secondary);cursor:pointer;border-bottom:2px solid transparent;margin-bottom:-2px}.dtab:hover{color:#1D9E75}.dtab.active{color:#1D9E75;border-bottom-color:#1D9E75}
.sec{padding:var(--space-4);margin-bottom:var(--space-3);box-shadow:0 2px 8px rgba(0,0,0,.06)}.sec h2{font:var(--font-h2);margin-bottom:var(--space-2)}.sec h3{font:var(--font-h3);margin-bottom:var(--space-3)}.sec-sub{font:var(--font-small);color:var(--color-text-secondary);margin-bottom:var(--space-3)}
.tcol{display:grid;grid-template-columns:1fr 1fr;gap:var(--space-3)}
.btn-sm{font-size:12px;padding:6px 14px}
.rd{font:var(--font-caption);font-weight:500;padding:2px 8px;border-radius:4px;white-space:nowrap}.rg{background:var(--color-success-bg);color:var(--color-success)}.ry{background:var(--color-warning-bg);color:var(--color-warning)}.rr{background:var(--color-danger-bg);color:var(--color-danger)}
.dot{width:8px;height:8px;border-radius:50%;display:inline-block;flex-shrink:0}.dg{background:var(--color-success)}.dy{background:var(--color-warning)}.dr{background:var(--color-danger)}
.sl-card{margin-bottom:var(--space-3)}.sl-top{display:flex;justify-content:space-between;align-items:flex-start;margin-bottom:var(--space-2)}.sl-ow{display:flex;align-items:center;gap:var(--space-2)}.sl-av{width:36px;height:36px;border-radius:50%;color:#fff;display:flex;align-items:center;justify-content:center;font-size:12px;font-weight:500}.sl-ow strong{font:var(--font-body);font-weight:500;display:block}.sl-tm{font:var(--font-caption);color:var(--color-text-tertiary)}.sl-pos{font:var(--font-small);color:var(--color-text-secondary);margin-bottom:var(--space-2)}.sl-bar{height:6px;background:var(--color-border);border-radius:3px;overflow:hidden;margin-bottom:var(--space-3)}.sl-fill{height:100%;border-radius:3px}.sl-cands{display:flex;gap:6px;flex-wrap:wrap;margin-bottom:var(--space-3)}.sl-c{display:flex;align-items:center;gap:6px;padding:4px 8px;background:var(--color-surface);border-radius:var(--radius-md);font:var(--font-caption)}.sl-ca{width:22px;height:22px;border-radius:50%;color:#fff;display:flex;align-items:center;justify-content:center;font-size:8px;font-weight:500}.sl-acts{display:flex;gap:var(--space-2)}
.dr-leg{display:flex;gap:var(--space-4);margin-bottom:var(--space-3);font:var(--font-caption);color:var(--color-text-secondary);align-items:center}.dr-leg span{display:flex;align-items:center;gap:4px}.dr-r{margin-left:auto;font-size:10px}.dr-cand{padding:var(--space-3) 0;border-bottom:1px solid var(--color-border);cursor:pointer}.dr-cand:last-child{border-bottom:none}.dr-cand:hover{background:rgba(0,0,0,.01)}.dr-row{display:flex;align-items:center;gap:var(--space-2)}.dr-av{width:36px;height:36px;border-radius:50%;color:#fff;display:flex;align-items:center;justify-content:center;font-size:12px;font-weight:500;flex-shrink:0}.dr-inf{flex:1}.dr-inf strong{font:var(--font-body);font-weight:500;display:block}.dr-inf span{font:var(--font-caption);color:var(--color-text-tertiary)}.dr-scs{display:flex;gap:4px}.dsc{width:28px;height:28px;border-radius:50%;display:flex;align-items:center;justify-content:center;font-size:10px;font-weight:600}.sai{border:1.5px solid var(--color-primary);color:var(--color-primary)}.scr{border:1.5px solid var(--color-success);color:var(--color-success)}.sdr{border:1.5px solid var(--color-accent);color:var(--color-accent)}.dr-cks{margin-top:var(--space-2);padding-left:44px}.dr-ck{display:flex;align-items:center;gap:6px;font:var(--font-caption);padding:3px 0}.ck-g{color:var(--color-success)}.ck-y{color:var(--color-warning)}.ck-r{color:var(--color-danger)}
.pm-row{display:flex;align-items:center;gap:var(--space-3);padding:var(--space-3) 0;border-bottom:1px solid var(--color-border)}.pm-row:last-child{border-bottom:none}.pm-pct{font-size:20px;font-weight:600;color:var(--color-primary);min-width:48px}.pm-inf{flex:1}.pm-inf strong{font:var(--font-body);font-weight:500;display:block}.pm-inf span{font:var(--font-caption);color:var(--color-text-tertiary)}
/* Gantt */
.gantt-controls{display:flex;gap:var(--space-3);margin-bottom:var(--space-3);flex-wrap:wrap}.gc-item{display:flex;flex-direction:column;gap:4px}.gc-item label{font:var(--font-caption);font-weight:500;color:var(--color-text-secondary)}.gc-select{padding:6px 10px;border:1px solid var(--color-border);border-radius:var(--radius-md);font:var(--font-small);background:var(--color-white);min-width:220px}
.v-header{display:flex;align-items:center;gap:var(--space-2);margin-bottom:var(--space-3)}.v-icon-w{width:40px;height:40px;border-radius:var(--radius-md);background:var(--color-surface);display:flex;align-items:center;justify-content:center;color:var(--color-text-secondary)}.v-header strong{font:var(--font-h3)}.v-meta{font:var(--font-caption);color:var(--color-text-tertiary)}
.v-kpis{display:flex;gap:8px;margin-bottom:var(--space-3);flex-wrap:wrap}.vk{flex:1;min-width:80px;background:var(--color-surface);border-radius:var(--radius-md);padding:8px;text-align:center}.vk-v{font-size:18px;font-weight:600}.vk-l{font-size:9px;color:var(--color-text-secondary);margin-top:2px}.vk-b{color:var(--color-primary)}.vk-d{color:var(--color-danger)}.vk-w{color:var(--color-warning)}.vk-g{color:var(--color-success)}
.gantt{border:1px solid var(--color-border);border-radius:var(--radius-md);overflow:hidden}.g-hdr{display:grid;grid-template-columns:110px repeat(6,1fr);border-bottom:1px solid var(--color-border);background:var(--color-surface)}.g-hdr span{padding:6px 4px;font:var(--font-caption);font-weight:500;color:var(--color-text-secondary);text-align:center}.g-hdr span:first-child{text-align:left;padding-left:10px}
.g-row{display:grid;grid-template-columns:110px 1fr;border-bottom:1px solid var(--color-border);min-height:34px;align-items:center}.g-row:last-child{border-bottom:none}.g-row:hover{background:rgba(0,0,0,.015)}.g-rank{padding:4px 10px;font:var(--font-caption);font-weight:500}.g-bars{position:relative;height:26px}.g-today{position:absolute;top:0;bottom:0;width:1.5px;background:#E24B4A;z-index:2}
.g-bar{position:absolute;top:3px;height:20px;border-radius:3px;display:flex;align-items:center;padding:0 5px;font-size:9px;font-weight:500;color:#fff;overflow:hidden;white-space:nowrap;cursor:pointer;transition:opacity .15s}.g-bar:hover{opacity:.85}.b-active{background:#0A66C2}.b-expiring{background:#E7A33E}.b-expired{background:#E24B4A}.b-planned{background:#1D9E75;border:1px dashed rgba(255,255,255,.5)}
.gantt-legend{display:flex;gap:14px;margin-top:10px;font:var(--font-caption);color:var(--color-text-secondary)}.gantt-legend span{display:flex;align-items:center;gap:4px}.gl-dot{width:10px;height:10px;border-radius:2px}
.cr{display:flex;justify-content:space-between;padding:var(--space-2) 0;border-bottom:1px solid var(--color-border);font:var(--font-small)}.cr:last-child{border-bottom:none}.cr span{color:var(--color-text-secondary)}.crt{border-top:2px solid var(--color-border);margin-top:var(--space-2);padding-top:var(--space-2)}
.al-item{padding:var(--space-2) 0;border-bottom:1px solid var(--color-border)}.al-item:last-child{border-bottom:none}.al-badge{font:var(--font-caption);font-weight:500;padding:2px 6px;border-radius:4px;display:inline-block;margin-bottom:2px}.al-urgent{background:var(--color-danger-bg);color:var(--color-danger)}.al-warn{background:var(--color-warning-bg);color:var(--color-warning)}.al-info{background:var(--color-primary-light);color:var(--color-primary)}.al-item p{font:var(--font-caption);color:var(--color-text-secondary)}

/* AI Intelligence */
.ai-subtabs{display:flex;gap:0;margin-bottom:var(--space-3);background:var(--color-surface);border-radius:var(--radius-md);overflow:hidden;border:1px solid var(--color-border)}
.ai-stab{flex:1;padding:8px 6px;background:none;border:none;font:var(--font-caption);font-weight:500;color:var(--color-text-secondary);cursor:pointer;border-right:1px solid var(--color-border);text-align:center}.ai-stab:last-child{border-right:none}.ai-stab:hover{background:var(--color-white)}.ai-stab.active{background:var(--color-white);color:#1D9E75;box-shadow:inset 0 -2px 0 #1D9E75}
.cand-select{display:flex;gap:8px;flex-wrap:wrap}.cand-opt{display:flex;align-items:center;gap:8px;padding:10px 14px;border:1.5px solid var(--color-border);border-radius:var(--radius-md);cursor:pointer;transition:all .15s;flex:1;min-width:180px}.cand-opt:hover{border-color:var(--color-primary)}.cand-opt.active{border-color:#1D9E75;background:rgba(29,158,117,0.05)}.cand-av{width:32px;height:32px;border-radius:50%;color:#fff;display:flex;align-items:center;justify-content:center;font-size:11px;font-weight:500}.cand-opt-info{flex:1}.cand-opt-info strong{display:block;font:var(--font-small);font-weight:500}.cand-opt-info span{font:var(--font-caption);color:var(--color-text-tertiary)}
.route-info{display:flex;align-items:center;gap:8px;margin-bottom:var(--space-3);padding:10px;background:var(--color-surface);border-radius:var(--radius-md)}.route-dot{width:8px;height:8px;border-radius:50%;display:inline-block;margin-right:4px}.route-from,.route-to{font:var(--font-small);font-weight:500}.route-arrow{color:var(--color-text-tertiary);font-size:16px}
.cost-breakdown{display:flex;flex-direction:column}.cb-row{display:flex;justify-content:space-between;padding:8px 0;border-bottom:1px solid var(--color-border);font:var(--font-small)}.cb-row:last-child{border-bottom:none}.cb-row span{color:var(--color-text-secondary)}.cb-warn{background:var(--color-warning-bg);margin:2px -12px;padding:8px 12px;border-radius:0}.cb-warn span{color:var(--color-warning)}.cb-warn strong{color:var(--color-warning)}.cb-total{border-top:2px solid var(--color-border);margin-top:4px;padding-top:10px}.cb-total strong{font-size:18px;color:var(--color-primary)}
.ai-note{margin-top:var(--space-2);padding:8px 10px;background:var(--color-surface);border-radius:var(--radius-md);font:var(--font-caption);color:var(--color-text-secondary);border-left:3px solid #1D9E75}
.cost-total-row{display:flex;justify-content:space-between;border-top:2px solid var(--color-border);margin-top:var(--space-2);padding-top:var(--space-2);font:var(--font-body)}.cost-total-row strong{font-size:18px;color:var(--color-primary)}
.cost-compare{display:grid;grid-template-columns:repeat(3,1fr);gap:8px}.cc-card{background:var(--color-surface);border-radius:var(--radius-md);padding:10px;text-align:center}.cc-best{border:1.5px solid var(--color-success)}.cc-val{font-size:20px;font-weight:600;color:var(--color-primary)}.cc-name{font:var(--font-caption);font-weight:500;margin-top:4px}.cc-route{font-size:10px;color:var(--color-text-tertiary)}
.savings-bar{height:24px;border-radius:4px;background:var(--color-border);overflow:hidden;margin:8px 0}.savings-fill{height:100%;border-radius:4px;background:#1B5E20;display:flex;align-items:center;padding:0 8px;font-size:10px;font-weight:500;color:#fff}
.cmp-grid{display:grid;grid-template-columns:120px repeat(auto-fit,minmax(100px,1fr));border:1px solid var(--color-border);border-radius:var(--radius-md);overflow:hidden;font:var(--font-caption)}.cmp-hdr{background:var(--color-surface);padding:8px;font-weight:500;text-align:center;border-bottom:1px solid var(--color-border);display:flex;align-items:center;justify-content:center;gap:4px}.cmp-hdr:first-child{text-align:left;justify-content:flex-start}.cmp-label{padding:6px 8px;font-weight:500;color:var(--color-text-secondary);background:var(--color-surface);border-bottom:1px solid var(--color-border)}.cmp-cell{padding:6px 8px;border-bottom:1px solid var(--color-border);text-align:center}.cmp-best{background:#E8F5E9;color:#1B5E20;font-weight:500}.cmp-warn{background:#FFF3E0;color:#E65100}.cmp-bad{background:#FFEBEE;color:#B71C1C}
.av-sm{width:22px;height:22px;border-radius:50%;color:#fff;display:inline-flex;align-items:center;justify-content:center;font-size:8px;font-weight:500}
.tl-kpis,.cl-kpis{display:flex;gap:8px;margin-bottom:var(--space-3)}.tl-kpis .mc,.cl-kpis .mc{flex:1}
.tl-steps{display:flex;flex-direction:column}.tl-step{display:flex;align-items:flex-start;gap:10px;padding:6px 0}.tl-dot{width:12px;height:12px;border-radius:50%;flex-shrink:0;margin-top:3px}.tld-done{background:#1B5E20}.tld-prog{background:#E7A33E}.tld-pend{background:var(--color-border)}.tl-info{flex:1;font:var(--font-caption)}.tl-info strong{display:block;font-size:12px}.tl-info span{color:var(--color-text-tertiary);font-size:10px}.tl-day{font:var(--font-caption);font-weight:500;min-width:40px;text-align:right}.tlt-done{color:#1B5E20}.tlt-prog{color:#E7A33E}.tlt-pend{color:var(--color-text-tertiary)}.tl-line-v{width:2px;height:16px;margin-left:5px;background:var(--color-border)}
.hist-row{display:flex;align-items:center;gap:8px;padding:8px 0;border-bottom:1px solid var(--color-border);font:var(--font-caption)}.hist-row:last-child{border-bottom:none}.hist-date{min-width:60px;color:var(--color-text-tertiary);font-size:10px}.hist-info{flex:1}.hist-info strong{display:block}.hist-info span{color:var(--color-text-tertiary);font-size:10px}.hist-cost{font-weight:500;color:var(--color-primary);min-width:45px;text-align:right}.hist-days{font-size:10px;color:var(--color-text-secondary);min-width:30px;text-align:right}
.cl-list{display:flex;flex-direction:column;gap:4px}.cl-item{display:flex;align-items:center;gap:8px;padding:4px 0;font:var(--font-caption)}.cl-icon{width:18px;height:18px;border-radius:4px;display:flex;align-items:center;justify-content:center;font-size:10px;font-weight:700;flex-shrink:0}.cli-done{background:#E8F5E9;color:#1B5E20}.cli-prog{background:#FFF3E0;color:#E65100}.cli-pend{background:var(--color-surface);color:var(--color-text-tertiary)}.cl-grey{color:var(--color-text-tertiary)}.cl-note{font-size:10px;color:#E65100;margin-left:4px}
.dtab[title]:hover,.ai-stab[title]:hover,.kc[title]:hover{position:relative}
[title]{cursor:help}
@media(max-width:1024px){.tcol{grid-template-columns:1fr}}@media(max-width:768px){.kg{grid-template-columns:repeat(2,1fr)}.g-hdr,.g-row{grid-template-columns:80px 1fr}.gantt-controls{flex-direction:column}.gc-select{min-width:100%}}
</style>

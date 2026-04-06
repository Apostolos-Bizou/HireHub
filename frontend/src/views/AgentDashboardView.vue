<script setup>
import { ref, computed } from 'vue'
const activeTab = ref(0)
const tabs = ['Shortlist analyzer', 'Deployment readiness', 'Principal matching', 'Crew calendar', 'Compliance']
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
</script>
<template>
<div class="container ad">
  <div class="ad-hdr"><div class="ad-hdr-l"><div class="avatar avatar-lg avatar-agent">MA</div><div><h1>Magsaysay Maritime Corp.</h1><p class="ad-sub">AI-powered deployment management · DMW-1234-2026</p></div></div></div>
  <div class="kg"><div class="kc"><div class="kl">Incoming requests</div><div class="kv" style="color:#B71C1C">4</div><div class="kt">2 urgent</div></div><div class="kc"><div class="kl">In pipeline</div><div class="kv" style="color:#0A66C2">18</div><div class="kt ku">+5 this week</div></div><div class="kc"><div class="kl">Ready to deploy</div><div class="kv" style="color:#1B5E20">6</div><div class="kt ku">All verified</div></div><div class="kc"><div class="kl">Avg deploy time</div><div class="kv">8.4 days</div><div class="kt ku">-2.1 vs last month</div></div><div class="kc"><div class="kl">Placement rate</div><div class="kv" style="color:#1B5E20">94%</div><div class="kt ku">+3% vs Q1</div></div><div class="kc"><div class="kl">Principals</div><div class="kv" style="color:#0A66C2">8</div><div class="kt">All DMW-verified</div></div><div class="kc"><div class="kl">Deployments YTD</div><div class="kv" style="color:#1D9E75">487</div><div class="kt ku">+42 this month</div></div><div class="kc"><div class="kl">Avg CrewScore placed</div><div class="kv" style="color:#1D9E75">81</div><div class="kt ku">Above avg</div></div></div>
  <div class="dtabs"><button v-for="(t,i) in tabs" :key="i" class="dtab" :class="{active:activeTab===i}" @click="activeTab=i">{{t}}</button></div>

  <!-- Tab 0: Shortlist Analyzer -->
  <div v-if="activeTab===0">
    <div v-for="sl in shortlists" :key="sl.owner" class="card sec sl-card">
      <div class="sl-top"><div class="sl-ow"><div class="sl-av" :style="{background:sl.color}">{{sl.i}}</div><div><strong>{{sl.owner}}</strong><span class="sl-tm">{{sl.time}} · {{sl.urgency}}</span></div></div><span class="rd" :class="sl.readyPct>=80?'rg':sl.readyPct>=50?'ry':'rr'">{{sl.readyPct}}% ready</span></div>
      <div class="sl-pos">{{sl.position}}</div>
      <div class="sl-bar"><div class="sl-fill" :style="{width:sl.readyPct+'%',background:sl.readyPct>=80?'#1B5E20':'#E7A33E'}"></div></div>
      <div class="sl-cands"><div v-for="c in sl.candidates" :key="c.i" class="sl-c"><div class="sl-ca" :style="{background:c.c}">{{c.i}}</div>{{c.n}}<span class="rd" :class="c.sc">{{c.s}}</span></div></div>
      <div class="sl-acts"><button class="btn btn-primary btn-sm">Review candidates</button><button class="btn btn-secondary btn-sm">Message owner</button></div>
    </div>
  </div>

  <!-- Tab 1: Deployment Readiness -->
  <div v-if="activeTab===1">
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
  <div v-if="activeTab===3">
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

  <!-- Tab 4: Compliance -->
  <div v-if="activeTab===4">
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
@media(max-width:1024px){.tcol{grid-template-columns:1fr}}@media(max-width:768px){.kg{grid-template-columns:repeat(2,1fr)}.g-hdr,.g-row{grid-template-columns:80px 1fr}.gantt-controls{flex-direction:column}.gc-select{min-width:100%}}
</style>

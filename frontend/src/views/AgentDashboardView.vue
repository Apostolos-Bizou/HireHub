<script setup>
import { ref } from 'vue'
const activeTab = ref(0)
const tabs = ['Shortlist analyzer', 'Deployment readiness', 'Principal matching', 'Crew calendar', 'Compliance']
const expandedCandidate = ref(null)

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
const calendar = [
  {vessel:'MT Varship Glory',owner:'Varship',rank:'3rd Officer',date:'May 15',days:39,cand:'Juan Dela Cruz',cost:2400,sc:'r-yellow'},
  {vessel:'MV Diana Explorer',owner:'Diana',rank:'Chief Engineer',date:'Jun 1',days:56,cand:'Dmitry Petrov',cost:3100,sc:'r-yellow'},
  {vessel:'MV Capital Fortune',owner:'Capital Ship',rank:'2nd Officer',date:'May 20',days:44,cand:'Andrei Marinescu',cost:2200,sc:'r-green'},
  {vessel:'MV Cosco Hellas',owner:'Costamare',rank:'Bosun',date:'Jul 10',days:95,cand:'TBD',cost:null,sc:'r-red'},
  {vessel:'MT Aegean Sun',owner:'Varship',rank:'AB',date:'May 28',days:52,cand:'Radu Ionescu',cost:1800,sc:'r-green'},
  {vessel:'MT Parthenon',owner:'Diana',rank:'3rd Engineer',date:'Jun 15',days:70,cand:'TBD',cost:null,sc:'r-red'}
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
function daysClass(d){return d<=30?'days-u':d<=60?'days-w':'days-ok'}
</script>
<template>
<div class="container ad">
  <div class="ad-hdr"><div class="ad-hdr-l"><div class="avatar avatar-lg avatar-agent">MA</div><div><h1>Magsaysay Maritime Corp.</h1><p class="ad-sub">AI-powered deployment management · DMW-1234-2026</p></div></div></div>
  <div class="kg"><div class="kc"><div class="kl">Incoming requests</div><div class="kv" style="color:#B71C1C">4</div><div class="kt">2 urgent</div></div><div class="kc"><div class="kl">In pipeline</div><div class="kv" style="color:#0A66C2">18</div><div class="kt ku">+5 this week</div></div><div class="kc"><div class="kl">Ready to deploy</div><div class="kv" style="color:#1B5E20">6</div><div class="kt ku">All verified</div></div><div class="kc"><div class="kl">Avg deploy time</div><div class="kv">8.4 days</div><div class="kt ku">-2.1 vs last month</div></div><div class="kc"><div class="kl">Placement rate</div><div class="kv" style="color:#1B5E20">94%</div><div class="kt ku">+3% vs Q1</div></div><div class="kc"><div class="kl">Principals</div><div class="kv" style="color:#0A66C2">8</div><div class="kt">All DMW-verified</div></div><div class="kc"><div class="kl">Deployments YTD</div><div class="kv" style="color:#1D9E75">487</div><div class="kt ku">+42 this month</div></div><div class="kc"><div class="kl">Avg CrewScore placed</div><div class="kv" style="color:#1D9E75">81</div><div class="kt ku">Above avg</div></div></div>
  <div class="dtabs"><button v-for="(t,i) in tabs" :key="i" class="dtab" :class="{active:activeTab===i}" @click="activeTab=i">{{t}}</button></div>

  <div v-if="activeTab===0">
    <div v-for="sl in shortlists" :key="sl.owner" class="card sec sl-card">
      <div class="sl-top"><div class="sl-ow"><div class="sl-av" :style="{background:sl.color}">{{sl.i}}</div><div><strong>{{sl.owner}}</strong><span class="sl-tm">{{sl.time}} · {{sl.urgency}}</span></div></div><span class="rd" :class="sl.readyPct>=80?'rg':sl.readyPct>=50?'ry':'rr'">{{sl.readyPct}}% ready</span></div>
      <div class="sl-pos">{{sl.position}}</div>
      <div class="sl-bar"><div class="sl-fill" :style="{width:sl.readyPct+'%',background:sl.readyPct>=80?'#1B5E20':'#E7A33E'}"></div></div>
      <div class="sl-cands"><div v-for="c in sl.candidates" :key="c.i" class="sl-c"><div class="sl-ca" :style="{background:c.c}">{{c.i}}</div>{{c.n}}<span class="rd" :class="c.sc">{{c.s}}</span></div></div>
      <div class="sl-acts"><button class="btn btn-primary btn-sm">Review candidates</button><button class="btn btn-secondary btn-sm">Message owner</button></div>
    </div>
  </div>

  <div v-if="activeTab===1">
    <div class="card sec">
      <div class="dr-leg"><span><span class="dot dg"></span> Ready</span><span><span class="dot dy"></span> Needs action</span><span><span class="dot dr"></span> Not ready</span><span class="dr-r">AI · Crew · DR scores</span></div>
      <div v-for="(c,ci) in readiness" :key="c.i" class="dr-cand" @click="expandedCandidate=expandedCandidate===ci?null:ci">
        <div class="dr-row"><div class="dr-av" :style="{background:c.c}">{{c.i}}</div><div class="dr-inf"><strong>{{c.n}}</strong><span>{{c.rank}} · {{c.nat}} · {{c.yrs}} yrs</span></div><div class="dr-scs"><div class="dsc sai">{{c.ai}}</div><div class="dsc scr">{{c.cr}}</div><div class="dsc sdr">{{c.dr}}</div></div><span class="rd" :class="c.sc">{{c.s}}</span></div>
        <div v-if="expandedCandidate===ci" class="dr-cks"><div v-for="ch in c.checks" :key="ch.t" class="dr-ck" :class="'ck-'+ch.o"><span class="dot" :class="'d'+ch.o"></span>{{ch.t}}</div></div>
      </div>
    </div>
  </div>

  <div v-if="activeTab===2">
    <div class="card sec">
      <h2>AI principal matching</h2><p class="sec-sub">AI analyzes hiring history and preferences to suggest best matches.</p>
      <div v-for="m in matches" :key="m.cand" class="pm-row"><div class="pm-pct">{{m.pct}}%</div><div class="pm-inf"><strong>{{m.cand}} → {{m.prin}}</strong><span>{{m.reason}}</span></div><span class="rd" :class="m.sc">{{m.pct>=85?'Auto-suggest':'Review'}}</span></div>
    </div>
  </div>

  <div v-if="activeTab===3">
    <div class="card sec">
      <h2>Crew change calendar</h2><p class="sec-sub">Scheduled crew changes with AI cost estimation.</p>
      <div class="cc-hd"><span class="cc1">Vessel</span><span class="cc2">Position</span><span class="cc3">Date</span><span class="cc4">Days</span><span class="cc5">Candidate</span><span class="cc6">Est. cost</span><span class="cc7">Status</span></div>
      <div v-for="cc in calendar" :key="cc.vessel+cc.rank" class="cc-rw">
        <div class="cc1"><strong>{{cc.vessel}}</strong><span>{{cc.owner}}</span></div>
        <span class="cc2">{{cc.rank}}</span><span class="cc3">{{cc.date}}</span>
        <span class="cc4" :class="daysClass(cc.days)">{{cc.days}}d</span>
        <span class="cc5" :class="{cctbd:cc.cand==='TBD'}">{{cc.cand}}</span>
        <span class="cc6">{{cc.cost?'$'+cc.cost.toLocaleString():'—'}}</span>
        <span class="cc7"><span class="rd" :class="cc.sc">{{cc.cand==='TBD'?'Searching':cc.days<=45?'In progress':'Ready'}}</span></span>
      </div>
    </div>
    <div class="tcol">
      <div class="card sec"><h3>Cost breakdown (typical)</h3><div class="cr"><span>Medical (PEME)</span><strong>$350</strong></div><div class="cr"><span>Travel Manila→Piraeus</span><strong>$890</strong></div><div class="cr"><span>Flag endorsement</span><strong>$160</strong></div><div class="cr"><span>Visa processing</span><strong>$200</strong></div><div class="cr"><span>DMW/OEC processing</span><strong>$150</strong></div><div class="cr"><span>Insurance</span><strong>$250</strong></div><div class="cr"><span>Agency fee</span><strong>$400</strong></div><div class="cr crt"><span>Estimated total</span><strong>$2,400</strong></div></div>
      <div class="card sec"><h3>Calendar summary</h3><div class="cr"><span>Next 30 days</span><strong style="color:#B71C1C">2 changes</strong></div><div class="cr"><span>30-60 days</span><strong style="color:#E65100">3 changes</strong></div><div class="cr"><span>60-90 days</span><strong>1 change</strong></div><div class="cr crt"><span>Pipeline cost</span><strong style="color:#0A66C2;font-size:18px">$13,700</strong></div></div>
    </div>
  </div>

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
.cc-hd,.cc-rw{display:grid;grid-template-columns:2fr 1.2fr 0.8fr 0.6fr 1.5fr 0.8fr 0.8fr;gap:var(--space-2);align-items:center;padding:var(--space-2) 0}.cc-hd{font:var(--font-caption);font-weight:600;color:var(--color-text-tertiary);text-transform:uppercase;letter-spacing:.5px;border-bottom:2px solid var(--color-border)}.cc-rw{border-bottom:1px solid var(--color-border);font:var(--font-small)}.cc-rw:last-child{border-bottom:none}.cc1 strong{display:block;font-weight:500}.cc1 span{font:var(--font-caption);color:var(--color-text-tertiary)}.cctbd{color:var(--color-danger);font-weight:500}.days-u{color:var(--color-danger);font-weight:600}.days-w{color:var(--color-warning);font-weight:500}.days-ok{color:var(--color-success)}
.cr{display:flex;justify-content:space-between;padding:var(--space-2) 0;border-bottom:1px solid var(--color-border);font:var(--font-small)}.cr:last-child{border-bottom:none}.cr span{color:var(--color-text-secondary)}.crt{border-top:2px solid var(--color-border);margin-top:var(--space-2);padding-top:var(--space-2)}
.al-item{padding:var(--space-2) 0;border-bottom:1px solid var(--color-border)}.al-item:last-child{border-bottom:none}.al-badge{font:var(--font-caption);font-weight:500;padding:2px 6px;border-radius:4px;display:inline-block;margin-bottom:2px}.al-urgent{background:var(--color-danger-bg);color:var(--color-danger)}.al-warn{background:var(--color-warning-bg);color:var(--color-warning)}.al-info{background:var(--color-primary-light);color:var(--color-primary)}.al-item p{font:var(--font-caption);color:var(--color-text-secondary)}
@media(max-width:1024px){.tcol{grid-template-columns:1fr}}@media(max-width:768px){.kg{grid-template-columns:repeat(2,1fr)}.cc-hd,.cc-rw{grid-template-columns:1fr 1fr}}
</style>

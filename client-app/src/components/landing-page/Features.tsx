import React from 'react';
import { motion } from 'framer-motion';
import { GitMerge, Globe, FileText, PieChart, TrendingDown, Receipt } from 'lucide-react';

export const Features: React.FC = () => {
  return (
    <section id="features" className="py-20 bg-[#1c1917] text-stone-50 overflow-hidden border-t border-white/5">
      <div className="max-w-7xl mx-auto px-6">
        <div className="mb-16 text-center max-w-2xl mx-auto">
          <motion.span 
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="text-orange-500 font-bold tracking-widest text-xs uppercase mb-4 block"
          >
            Core Capabilities
          </motion.span>
          <motion.h2 
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: 0.1 }}
            className="text-3xl md:text-4xl font-serif mb-6 text-white"
          >
            Powering the next generation <br /> of asset management.
          </motion.h2>
        </div>

        {/* Top Row: Major Features */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
          
          {/* 1. Asset Map */}
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            className="bg-[#14161b] rounded-3xl p-10 border border-stone-800 hover:border-stone-700 transition-all duration-500 group relative overflow-hidden flex flex-col min-h-[400px]"
          >
             <div className="absolute inset-0 bg-[radial-gradient(circle_at_0%_0%,rgba(234,88,12,0.08),transparent_50%)]"></div>
             
             <div className="relative z-10 mb-12">
               <div className="w-14 h-14 rounded-2xl bg-orange-500/10 text-orange-500 flex items-center justify-center mb-8 border border-orange-500/20 shadow-lg shadow-orange-900/20">
                  <Globe size={28} />
               </div>
               <h3 className="text-3xl font-serif mb-4 text-white">Global Asset Intelligence</h3>
               <p className="text-stone-400 text-lg leading-relaxed font-light">
                 Real-time telemetry for every asset in your portfolio. Monitor location, status, and utilization across borders with satellite-level precision.
               </p>
             </div>

             {/* Grid visualization */}
             <div className="flex-1 relative bg-[#0c0a09] rounded-2xl border border-stone-800 overflow-hidden group-hover:border-stone-700 transition-colors">
                <div className="absolute inset-0" style={{ backgroundImage: 'radial-gradient(#292524 1px, transparent 1px)', backgroundSize: '30px 30px' }}></div>
                
                {/* Pulsating dots */}
                {[
                  { top: '30%', left: '20%' },
                  { top: '60%', left: '50%' },
                  { top: '40%', left: '70%' },
                  { top: '20%', left: '80%' },
                  { top: '70%', left: '30%' }
                ].map((pos, i) => (
                  <div key={i} className="absolute w-4 h-4" style={pos}>
                     <div className="absolute inset-0 bg-orange-500 rounded-full animate-ping opacity-20"></div>
                     <div className="relative w-4 h-4 bg-orange-600 rounded-full border-2 border-[#0c0a09] shadow-lg shadow-orange-500/20"></div>
                  </div>
                ))}

                {/* Connection Lines */}
                <svg className="absolute inset-0 w-full h-full pointer-events-none opacity-20">
                   <path d="M150 100 Q 300 150 500 120" stroke="#ea580c" strokeWidth="2" fill="none" strokeDasharray="5 5" />
                   <path d="M500 120 Q 400 250 250 200" stroke="#ea580c" strokeWidth="2" fill="none" strokeDasharray="5 5" />
                </svg>
             </div>
          </motion.div>

          {/* 2. Workflow Logic */}
          <motion.div
             initial={{ opacity: 0, x: 20 }}
             whileInView={{ opacity: 1, x: 0 }}
             viewport={{ once: true }}
             transition={{ delay: 0.1 }}
             className="bg-[#14161b] rounded-3xl p-10 border border-stone-800 hover:border-stone-700 transition-all duration-500 group relative overflow-hidden flex flex-col min-h-[500px]"
          >
             {/* Subtle Green Gradient for 'Logic/Success' */}
             <div className="absolute top-0 right-0 p-12 opacity-5 pointer-events-none">
               <GitMerge size={300} />
             </div>
             <div className="absolute inset-0 bg-[radial-gradient(circle_at_100%_0%,rgba(16,185,129,0.05),transparent_50%)]"></div>
             
             <div className="relative z-10 mb-12">
                <div className="w-14 h-14 rounded-2xl bg-emerald-500/10 text-emerald-500 flex items-center justify-center mb-8 border border-emerald-500/20 shadow-lg shadow-emerald-900/20">
                  <GitMerge size={28} />
                </div>
                <h3 className="text-3xl font-serif mb-4 text-white">Smart Workflows</h3>
                <p className="text-stone-400 text-lg leading-relaxed font-light">
                    The "If-This-Then-That" engine for your physical assets. Trigger complex approval chains based on asset value, location, or type automatically.
                </p>
             </div>

             <div className="flex-1 flex flex-col justify-end space-y-4 relative">
                <div className="absolute left-4 top-4 bottom-8 w-px bg-stone-800"></div>
                
                {[
                  { label: 'Asset Request Initiated', color: 'bg-stone-800 text-stone-400 border-stone-700' },
                  { label: 'Value Check > $10k', color: 'bg-stone-800/80 text-stone-300 border-stone-700' },
                  { label: 'CFO Approval Required', color: 'bg-stone-800/80 text-stone-300 border-stone-700' },
                  { label: 'Transfer Executed', color: 'bg-emerald-900/20 text-emerald-400 border-emerald-800/50' }
                ].map((step, i) => (
                   <div key={i} className={`relative z-10 flex items-center gap-4 p-4 rounded-xl border ${step.color.includes('border') ? step.color : 'bg-stone-800 border-stone-700'} text-sm font-medium shadow-lg backdrop-blur-sm transition-all duration-300 hover:translate-x-1`}>
                      <div className={`w-2.5 h-2.5 rounded-full ${i===3 ? 'bg-emerald-500 shadow-[0_0_8px_rgba(16,185,129,0.6)]' : 'bg-stone-600'}`}></div>
                      {step.label}
                   </div>
                ))}
             </div>
          </motion.div>

        </div>

        {/* Bottom Row: Financial Features */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            
            {/* Invoice Management */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: 0.2 }}
              className="bg-[#14161b] rounded-3xl p-8 border border-stone-800 hover:border-stone-700 transition-colors group min-h-[400px] flex flex-col"
            >
                <div className="w-12 h-12 rounded-xl bg-stone-800 text-stone-300 flex items-center justify-center mb-6 border border-stone-700 group-hover:scale-110 transition-transform duration-300">
                    <Receipt size={24} />
                </div>
                <h3 className="text-xl font-serif mb-3 text-white">Invoice Command</h3>
                <p className="text-stone-500 text-sm leading-relaxed mb-8">
                    Centralize procurement. Auto-match invoices to purchase orders and asset records instantly.
                </p>
                <div className="flex-1 bg-[#0c0a09] rounded-xl border border-stone-800 p-4 flex flex-col gap-3 relative overflow-hidden">
                     <div className="absolute top-0 left-0 w-full h-full bg-gradient-to-b from-transparent to-[#0c0a09] pointer-events-none z-10"></div>
                     {[1, 2, 3].map((i) => (
                         <div key={i} className="flex items-center justify-between p-3 rounded-lg bg-stone-900/50 border border-stone-800/50">
                             <div className="flex items-center gap-3">
                                 <div className="w-8 h-8 rounded bg-stone-800 flex items-center justify-center">
                                     <FileText size={14} className="text-stone-500" />
                                 </div>
                                 <div className="flex flex-col">
                                     <span className="text-xs text-white font-medium">INV-2024-00{i}</span>
                                     <span className="text-[10px] text-stone-500">Vendor Corp</span>
                                 </div>
                             </div>
                             <div className="px-2 py-1 rounded bg-emerald-500/10 text-emerald-500 text-[10px] font-medium border border-emerald-500/20">
                                 PAID
                             </div>
                         </div>
                     ))}
                </div>
            </motion.div>

            {/* Financial Control */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: 0.3 }}
              className="bg-[#14161b] rounded-3xl p-8 border border-stone-800 hover:border-stone-700 transition-colors group min-h-[400px] flex flex-col"
            >
                <div className="w-12 h-12 rounded-xl bg-stone-800 text-stone-300 flex items-center justify-center mb-6 border border-stone-700 group-hover:scale-110 transition-transform duration-300">
                    <PieChart size={24} />
                </div>
                <h3 className="text-xl font-serif mb-3 text-white">Financial Control</h3>
                <p className="text-stone-500 text-sm leading-relaxed mb-8">
                    Real-time budget tracking. Visualize CapEx vs OpEx and identify cost leakage immediately.
                </p>
                <div className="flex-1 flex items-center justify-center bg-[#0c0a09] rounded-xl border border-stone-800 p-4 relative">
                    <div className="relative w-32 h-32">
                        <svg viewBox="0 0 100 100" className="transform -rotate-90 w-full h-full">
                            <circle cx="50" cy="50" r="40" stroke="#292524" strokeWidth="12" fill="none" />
                            <circle cx="50" cy="50" r="40" stroke="#ea580c" strokeWidth="12" fill="none" strokeDasharray="100 251" className="animate-[dash_1.5s_ease-out_forwards]" strokeLinecap="round" />
                            <circle cx="50" cy="50" r="40" stroke="#10b981" strokeWidth="12" fill="none" strokeDasharray="60 251" strokeDashoffset="-100" className="animate-[dash_1.8s_ease-out_forwards]" strokeLinecap="round" />
                        </svg>
                        <div className="absolute inset-0 flex items-center justify-center">
                             <span className="text-white font-bold text-lg">84%</span>
                        </div>
                    </div>
                    <div className="absolute bottom-4 left-4 text-[10px] font-mono text-stone-500">
                        <div>● CapEx Used</div>
                    </div>
                </div>
            </motion.div>

            {/* Depreciation */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: 0.4 }}
              className="bg-[#14161b] rounded-3xl p-8 border border-stone-800 hover:border-stone-700 transition-colors group min-h-[400px] flex flex-col"
            >
                <div className="w-12 h-12 rounded-xl bg-stone-800 text-stone-300 flex items-center justify-center mb-6 border border-stone-700 group-hover:scale-110 transition-transform duration-300">
                    <TrendingDown size={24} />
                </div>
                <h3 className="text-xl font-serif mb-3 text-white">Auto-Depreciation</h3>
                <p className="text-stone-500 text-sm leading-relaxed mb-8">
                    Set it and forget it. Straight-line or declining balance schedules calculated automatically.
                </p>
                <div className="flex-1 bg-[#0c0a09] rounded-xl border border-stone-800 p-6 flex items-end relative">
                    <div className="absolute inset-0 flex items-center justify-center opacity-10">
                         <div className="w-full h-px bg-stone-700 absolute top-1/4"></div>
                         <div className="w-full h-px bg-stone-700 absolute top-2/4"></div>
                         <div className="w-full h-px bg-stone-700 absolute top-3/4"></div>
                    </div>
                    <svg className="w-full h-32 overflow-visible">
                         {/* Curve */}
                         <path 
                            d="M0,10 Q100,20 250,120" 
                            fill="none" 
                            stroke="#ef4444" 
                            strokeWidth="3" 
                            strokeLinecap="round"
                         />
                         {/* Area under curve */}
                         <path 
                            d="M0,10 Q100,20 250,120 L250,130 L0,130 Z" 
                            fill="url(#depreciationGradient)" 
                            opacity="0.2"
                         />
                         <defs>
                             <linearGradient id="depreciationGradient" x1="0" y1="0" x2="0" y2="1">
                                 <stop offset="0%" stopColor="#ef4444"/>
                                 <stop offset="100%" stopColor="transparent"/>
                             </linearGradient>
                         </defs>
                         {/* Dots */}
                         <circle cx="0" cy="10" r="4" fill="#1c1917" stroke="#ef4444" strokeWidth="2" />
                         <circle cx="125" cy="45" r="4" fill="#1c1917" stroke="#ef4444" strokeWidth="2" />
                         <circle cx="250" cy="120" r="4" fill="#1c1917" stroke="#ef4444" strokeWidth="2" />
                    </svg>
                </div>
            </motion.div>

        </div>

      </div>
    </section>
  );
};

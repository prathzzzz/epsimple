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
            whileHover={{ scale: 1.02, transition: { duration: 0.3 } }}
            className="bg-gradient-to-br from-[#14161b] via-[#1a1c22] to-[#14161b] rounded-3xl p-10 border border-stone-800 hover:border-orange-500/50 transition-all duration-500 group relative overflow-hidden flex flex-col min-h-[400px] shadow-xl hover:shadow-orange-500/20"
          >
             <div className="absolute inset-0 bg-[radial-gradient(circle_at_0%_0%,rgba(234,88,12,0.15),transparent_50%)] group-hover:bg-[radial-gradient(circle_at_0%_0%,rgba(234,88,12,0.25),transparent_60%)] transition-all duration-500"></div>
             <div className="absolute top-0 right-0 w-96 h-96 bg-orange-500/10 blur-[120px] rounded-full opacity-0 group-hover:opacity-100 transition-opacity duration-700"></div>
             
             <div className="relative z-10 mb-12">
               <motion.div 
                 className="w-14 h-14 rounded-2xl bg-gradient-to-br from-orange-500/20 to-orange-600/10 text-orange-500 flex items-center justify-center mb-8 border border-orange-500/30 shadow-lg shadow-orange-500/30 group-hover:shadow-orange-500/50 transition-shadow"
                 whileHover={{ rotate: 360 }}
                 transition={{ duration: 0.6 }}
               >
                  <Globe size={28} className="group-hover:scale-110 transition-transform" />
               </motion.div>
               <h3 className="text-3xl font-serif mb-4 bg-gradient-to-r from-white to-orange-100 bg-clip-text text-transparent">Track Assets</h3>
               <p className="text-stone-400 text-lg leading-relaxed font-light">
                 Real-time tracking of every asset in your portfolio. Monitor location, status, condition, and utilization with precision. Get instant alerts on movements and status changes.
               </p>
             </div>

             {/* Grid visualization */}
             <div className="flex-1 relative bg-[#0c0a09] rounded-2xl border border-stone-800 overflow-hidden group-hover:border-stone-700 transition-colors">
                <div className="absolute inset-0" style={{ backgroundImage: 'radial-gradient(#292524 1px, transparent 1px)', backgroundSize: '30px 30px' }}></div>
                
                {/* Pulsating dots */}
                {[
                  { top: '30%', left: '20%', delay: '0s', color: 'bg-orange-500' },
                  { top: '60%', left: '50%', delay: '0.2s', color: 'bg-yellow-500' },
                  { top: '40%', left: '70%', delay: '0.4s', color: 'bg-red-500' },
                  { top: '20%', left: '80%', delay: '0.6s', color: 'bg-pink-500' },
                  { top: '70%', left: '30%', delay: '0.8s', color: 'bg-amber-500' }
                ].map((pos, i) => (
                  <motion.div 
                    key={i} 
                    className="absolute w-4 h-4" 
                    style={{ top: pos.top, left: pos.left }}
                    initial={{ scale: 0, opacity: 0 }}
                    whileInView={{ scale: 1, opacity: 1 }}
                    transition={{ delay: i * 0.2, duration: 0.5 }}
                    viewport={{ once: true }}
                  >
                     <div className={`absolute inset-0 ${pos.color} rounded-full animate-ping opacity-30`} style={{ animationDelay: pos.delay }}></div>
                     <div className={`relative w-4 h-4 ${pos.color} rounded-full border-2 border-[#0c0a09] shadow-lg shadow-${pos.color.split('-')[1]}-500/40 group-hover:scale-125 transition-transform`}></div>
                  </motion.div>
                ))}

                {/* Connection Lines */}
                <svg className="absolute inset-0 w-full h-full pointer-events-none opacity-20">
                   <motion.path 
                     d="M150 100 Q 300 150 500 120" 
                     stroke="#ea580c" 
                     strokeWidth="2" 
                     fill="none" 
                     strokeDasharray="5 5"
                     initial={{ pathLength: 0 }}
                     whileInView={{ pathLength: 1 }}
                     transition={{ duration: 2, ease: "easeInOut" }}
                     viewport={{ once: true }}
                   />
                   <motion.path 
                     d="M500 120 Q 400 250 250 200" 
                     stroke="#ea580c" 
                     strokeWidth="2" 
                     fill="none" 
                     strokeDasharray="5 5"
                     initial={{ pathLength: 0 }}
                     whileInView={{ pathLength: 1 }}
                     transition={{ duration: 2, delay: 0.5, ease: "easeInOut" }}
                     viewport={{ once: true }}
                   />
                </svg>
             </div>
          </motion.div>

          {/* 2. Workflow Logic */}
          <motion.div
             initial={{ opacity: 0, x: 20 }}
             whileInView={{ opacity: 1, x: 0 }}
             viewport={{ once: true }}
             transition={{ delay: 0.1 }}
             whileHover={{ scale: 1.02, transition: { duration: 0.3 } }}
             className="bg-gradient-to-br from-[#14161b] via-[#161a1f] to-[#14161b] rounded-3xl p-10 border border-stone-800 hover:border-emerald-500/50 transition-all duration-500 group relative overflow-hidden flex flex-col min-h-[500px] shadow-xl hover:shadow-emerald-500/20"
          >
             {/* Subtle Green Gradient for 'Logic/Success' */}
             <motion.div 
               className="absolute top-0 right-0 p-12 opacity-5 pointer-events-none group-hover:opacity-10 transition-opacity"
               animate={{ rotate: 360 }}
               transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
             >
               <GitMerge size={300} />
             </motion.div>
             <div className="absolute inset-0 bg-[radial-gradient(circle_at_100%_0%,rgba(16,185,129,0.1),transparent_50%)] group-hover:bg-[radial-gradient(circle_at_100%_0%,rgba(16,185,129,0.2),transparent_60%)] transition-all duration-500"></div>
             <div className="absolute bottom-0 left-0 w-96 h-96 bg-emerald-500/10 blur-[120px] rounded-full opacity-0 group-hover:opacity-100 transition-opacity duration-700"></div>
             
             <div className="relative z-10 mb-12">
                <motion.div 
                  className="w-14 h-14 rounded-2xl bg-gradient-to-br from-emerald-500/20 to-teal-600/10 text-emerald-500 flex items-center justify-center mb-8 border border-emerald-500/30 shadow-lg shadow-emerald-500/30 group-hover:shadow-emerald-500/50 transition-shadow"
                  whileHover={{ rotate: 180 }}
                  transition={{ duration: 0.6 }}
                >
                  <GitMerge size={28} className="group-hover:scale-110 transition-transform" />
                </motion.div>
                <h3 className="text-3xl font-serif mb-4 bg-gradient-to-r from-white to-emerald-100 bg-clip-text text-transparent">Smart Workflows</h3>
                <p className="text-stone-400 text-lg leading-relaxed font-light">
                    The "If-This-Then-That" engine for your physical assets. Trigger complex approval chains based on asset value, location, or type automatically.
                </p>
             </div>

             <div className="flex-1 flex flex-col justify-end space-y-4 relative">
                <div className="absolute left-4 top-4 bottom-8 w-px bg-gradient-to-b from-stone-700 via-emerald-500/30 to-emerald-500"></div>
                
                {[
                  { label: 'Asset Request Initiated', color: 'bg-stone-800 text-stone-400 border-stone-700' },
                  { label: 'Value Check > $10k', color: 'bg-stone-800/80 text-stone-300 border-stone-700' },
                  { label: 'CFO Approval Required', color: 'bg-stone-800/80 text-stone-300 border-stone-700' },
                  { label: 'Transfer Executed', color: 'bg-emerald-900/20 text-emerald-400 border-emerald-800/50' }
                ].map((step, i) => (
                   <motion.div 
                     key={i} 
                     initial={{ opacity: 0, x: -20 }}
                     whileInView={{ opacity: 1, x: 0 }}
                     transition={{ delay: i * 0.15, duration: 0.5 }}
                     viewport={{ once: true }}
                     className={`relative z-10 flex items-center gap-4 p-4 rounded-xl border ${step.color.includes('border') ? step.color : 'bg-stone-800 border-stone-700'} text-sm font-medium shadow-lg backdrop-blur-sm transition-all duration-300 hover:translate-x-1`}
                   >
                      <div className={`w-2.5 h-2.5 rounded-full ${i===3 ? 'bg-emerald-500 shadow-[0_0_8px_rgba(16,185,129,0.6)] animate-pulse' : 'bg-stone-600'}`}></div>
                      {step.label}
                   </motion.div>
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
              whileHover={{ y: -5 }}
              className="bg-gradient-to-br from-[#14161b] to-[#1a1520] rounded-3xl p-8 border border-stone-800 hover:border-purple-500/50 transition-all duration-300 group min-h-[400px] flex flex-col shadow-lg hover:shadow-purple-500/20 relative overflow-hidden"
            >
                <div className="absolute top-0 right-0 w-64 h-64 bg-purple-500/10 blur-[100px] rounded-full opacity-0 group-hover:opacity-100 transition-opacity duration-700"></div>
                <motion.div 
                  className="w-12 h-12 rounded-xl bg-gradient-to-br from-purple-500/20 to-pink-500/10 text-purple-400 flex items-center justify-center mb-6 border border-purple-500/30 shadow-lg shadow-purple-500/20 relative z-10"
                  whileHover={{ rotate: 360, scale: 1.1 }}
                  transition={{ duration: 0.5 }}
                >
                    <Receipt size={24} />
                </motion.div>
                <h3 className="text-xl font-serif mb-3 bg-gradient-to-r from-white to-purple-200 bg-clip-text text-transparent relative z-10">Invoice Command</h3>
                <p className="text-stone-500 text-sm leading-relaxed mb-8">
                    Centralize procurement. Auto-match invoices to purchase orders and asset records instantly.
                </p>
                <div className="flex-1 bg-[#0c0a09] rounded-xl border border-stone-800 p-4 flex flex-col gap-3 relative overflow-hidden">
                     <div className="absolute top-0 left-0 w-full h-full bg-gradient-to-b from-transparent to-[#0c0a09] pointer-events-none z-10"></div>
                     {[1, 2, 3].map((i) => (
                         <motion.div 
                           key={i} 
                           initial={{ opacity: 0, y: 10 }}
                           whileInView={{ opacity: 1, y: 0 }}
                           transition={{ delay: 0.3 + (i * 0.1), duration: 0.4 }}
                           viewport={{ once: true }}
                           className="flex items-center justify-between p-3 rounded-lg bg-stone-900/50 border border-stone-800/50 hover:bg-stone-900/70 hover:border-stone-700/50 transition-colors"
                         >
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
                         </motion.div>
                     ))}
                </div>
            </motion.div>

            {/* Financial Control */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: 0.3 }}
              whileHover={{ y: -5 }}
              className="bg-gradient-to-br from-[#14161b] via-[#1a1620] to-[#14161b] rounded-3xl p-8 border border-stone-800 hover:border-blue-500/50 transition-all duration-300 group min-h-[400px] flex flex-col shadow-lg hover:shadow-blue-500/20 relative overflow-hidden"
            >
                <div className="absolute top-0 left-0 w-64 h-64 bg-blue-500/10 blur-[100px] rounded-full opacity-0 group-hover:opacity-100 transition-opacity duration-700"></div>
                <motion.div 
                  className="w-12 h-12 rounded-xl bg-gradient-to-br from-blue-500/20 to-cyan-500/10 text-blue-400 flex items-center justify-center mb-6 border border-blue-500/30 shadow-lg shadow-blue-500/20 relative z-10"
                  whileHover={{ rotate: 360, scale: 1.1 }}
                  transition={{ duration: 0.5 }}
                >
                    <PieChart size={24} />
                </motion.div>
                <h3 className="text-xl font-serif mb-3 bg-gradient-to-r from-white to-blue-200 bg-clip-text text-transparent relative z-10">Financial Control</h3>
                <p className="text-stone-500 text-sm leading-relaxed mb-8">
                    Real-time budget tracking. Visualize CapEx vs OpEx and identify cost leakage immediately.
                </p>
                <div className="flex-1 flex items-center justify-center bg-[#0c0a09] rounded-xl border border-stone-800 p-4 relative overflow-hidden group-hover:border-blue-500/30 transition-colors">
                    <div className="absolute inset-0 bg-gradient-to-r from-blue-500/5 via-purple-500/5 to-cyan-500/5 opacity-0 group-hover:opacity-100 transition-opacity"></div>
                    <div className="relative w-32 h-32">
                        <svg viewBox="0 0 100 100" className="transform -rotate-90 w-full h-full">
                            <circle cx="50" cy="50" r="40" stroke="#292524" strokeWidth="12" fill="none" />
                            <motion.circle 
                              cx="50" 
                              cy="50" 
                              r="40" 
                              stroke="#ea580c" 
                              strokeWidth="12" 
                              fill="none" 
                              strokeDasharray="100 251" 
                              strokeLinecap="round"
                              initial={{ strokeDashoffset: 251 }}
                              whileInView={{ strokeDashoffset: 151 }}
                              transition={{ duration: 1.5, ease: "easeOut", delay: 0.3 }}
                              viewport={{ once: true }}
                            />
                            <motion.circle 
                              cx="50" 
                              cy="50" 
                              r="40" 
                              stroke="#10b981" 
                              strokeWidth="12" 
                              fill="none" 
                              strokeDasharray="60 251" 
                              strokeDashoffset="-100" 
                              strokeLinecap="round"
                              initial={{ strokeDashoffset: 151 }}
                              whileInView={{ strokeDashoffset: 91 }}
                              transition={{ duration: 1.8, ease: "easeOut", delay: 0.5 }}
                              viewport={{ once: true }}
                            />
                        </svg>
                        <motion.div 
                          className="absolute inset-0 flex items-center justify-center"
                          initial={{ scale: 0 }}
                          whileInView={{ scale: 1 }}
                          transition={{ delay: 0.8, duration: 0.5, type: "spring" }}
                          viewport={{ once: true }}
                        >
                             <span className="text-white font-bold text-lg">84%</span>
                        </motion.div>
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
              whileHover={{ y: -5 }}
              className="bg-gradient-to-br from-[#14161b] via-[#1c1418] to-[#14161b] rounded-3xl p-8 border border-stone-800 hover:border-red-500/50 transition-all duration-300 group min-h-[400px] flex flex-col shadow-lg hover:shadow-red-500/20 relative overflow-hidden"
            >
                <div className="absolute bottom-0 right-0 w-64 h-64 bg-red-500/10 blur-[100px] rounded-full opacity-0 group-hover:opacity-100 transition-opacity duration-700"></div>
                <motion.div 
                  className="w-12 h-12 rounded-xl bg-gradient-to-br from-red-500/20 to-orange-500/10 text-red-400 flex items-center justify-center mb-6 border border-red-500/30 shadow-lg shadow-red-500/20 relative z-10"
                  whileHover={{ rotate: -360, scale: 1.1 }}
                  transition={{ duration: 0.5 }}
                >
                    <TrendingDown size={24} />
                </motion.div>
                <h3 className="text-xl font-serif mb-3 bg-gradient-to-r from-white to-red-200 bg-clip-text text-transparent relative z-10">Auto-Depreciation</h3>
                <p className="text-stone-500 text-sm leading-relaxed mb-8">
                    Set it and forget it. Straight-line or declining balance schedules calculated automatically.
                </p>
                <div className="flex-1 bg-[#0c0a09] rounded-xl border border-stone-800 p-6 flex items-end relative overflow-hidden group-hover:border-red-500/30 transition-colors">
                    <div className="absolute inset-0 bg-gradient-to-br from-red-500/5 via-transparent to-orange-500/5 opacity-0 group-hover:opacity-100 transition-opacity"></div>
                    <div className="absolute inset-0 flex items-center justify-center opacity-10">
                         <div className="w-full h-px bg-stone-700 absolute top-1/4"></div>
                         <div className="w-full h-px bg-stone-700 absolute top-2/4"></div>
                         <div className="w-full h-px bg-stone-700 absolute top-3/4"></div>
                    </div>
                    <svg className="w-full h-32 overflow-visible">
                         <defs>
                             <linearGradient id="depreciationGradient" x1="0" y1="0" x2="0" y2="1">
                                 <stop offset="0%" stopColor="#ef4444"/>
                                 <stop offset="100%" stopColor="transparent"/>
                             </linearGradient>
                         </defs>
                         {/* Area under curve */}
                         <motion.path 
                            d="M0,10 Q100,20 250,120 L250,130 L0,130 Z" 
                            fill="url(#depreciationGradient)" 
                            opacity="0.2"
                            initial={{ opacity: 0 }}
                            whileInView={{ opacity: 0.2 }}
                            transition={{ duration: 1, delay: 0.5 }}
                            viewport={{ once: true }}
                         />
                         {/* Curve */}
                         <motion.path 
                            d="M0,10 Q100,20 250,120" 
                            fill="none" 
                            stroke="#ef4444" 
                            strokeWidth="3" 
                            strokeLinecap="round"
                            initial={{ pathLength: 0 }}
                            whileInView={{ pathLength: 1 }}
                            transition={{ duration: 2, ease: "easeInOut", delay: 0.3 }}
                            viewport={{ once: true }}
                         />
                         {/* Dots */}
                         <motion.circle 
                           cx="0" 
                           cy="10" 
                           r="4" 
                           fill="#1c1917" 
                           stroke="#ef4444" 
                           strokeWidth="2"
                           initial={{ scale: 0 }}
                           whileInView={{ scale: 1 }}
                           transition={{ delay: 0.3, type: "spring" }}
                           viewport={{ once: true }}
                         />
                         <motion.circle 
                           cx="125" 
                           cy="45" 
                           r="4" 
                           fill="#1c1917" 
                           stroke="#ef4444" 
                           strokeWidth="2"
                           initial={{ scale: 0 }}
                           whileInView={{ scale: 1 }}
                           transition={{ delay: 1.3, type: "spring" }}
                           viewport={{ once: true }}
                         >
                           <animate attributeName="r" values="4;5;4" dur="2s" repeatCount="indefinite" />
                         </motion.circle>
                         <motion.circle 
                           cx="250" 
                           cy="120" 
                           r="4" 
                           fill="#1c1917" 
                           stroke="#ef4444" 
                           strokeWidth="2"
                           initial={{ scale: 0 }}
                           whileInView={{ scale: 1 }}
                           transition={{ delay: 2.3, type: "spring" }}
                           viewport={{ once: true }}
                         />
                    </svg>
                </div>
            </motion.div>

        </div>

      </div>
    </section>
  );
};

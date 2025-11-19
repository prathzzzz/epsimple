import React from 'react';
import { APP_NAME } from '../constants';

export const Footer: React.FC = () => {
  return (
    <footer className="bg-[#0c0a09] text-stone-400 py-12 border-t border-white/5 relative overflow-hidden">
       {/* Decorative top gradient */}
       <div className="absolute top-0 left-1/2 -translate-x-1/2 w-full max-w-3xl h-px bg-gradient-to-r from-transparent via-stone-700 to-transparent"></div>
       
      <div className="max-w-6xl mx-auto px-6 flex flex-col md:flex-row items-center justify-between gap-8">
        <div className="flex items-center gap-3">
           <div className="w-8 h-8 bg-stone-800 rounded-lg flex items-center justify-center border border-stone-700 shadow-inner group">
               <div className="w-2 h-2 bg-orange-600 rounded-full shadow-[0_0_10px_rgba(234,88,12,0.5)] group-hover:shadow-[0_0_15px_rgba(234,88,12,0.8)] transition-shadow duration-500" />
            </div>
            <span className="text-xl font-serif font-medium text-stone-200 tracking-tight">{APP_NAME}</span>
        </div>
        
        <div className="flex items-center gap-8 text-sm font-medium text-stone-500">
            <a href="#" className="hover:text-stone-300 transition-colors">Privacy Policy</a>
            <a href="#" className="hover:text-stone-300 transition-colors">Terms of Service</a>
            <a href="#" className="hover:text-stone-300 transition-colors">Contact Support</a>
        </div>

        <div className="text-xs text-stone-600 font-mono">
            © {new Date().getFullYear()} EpsOne Systems.
        </div>
      </div>
    </footer>
  );
};
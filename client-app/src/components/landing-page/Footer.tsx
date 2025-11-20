import React from 'react';
import { Logo } from '@/components/logo';

export const Footer: React.FC = () => {
  return (
    <footer className="text-stone-700 py-12 border-t border-stone-200 relative overflow-hidden">
       {/* Decorative top gradient */}
       <div className="absolute top-0 left-1/2 -translate-x-1/2 w-full max-w-3xl h-px bg-gradient-to-r from-transparent via-stone-300 to-transparent"></div>
       
      <div className="max-w-6xl mx-auto px-6 flex flex-col md:flex-row items-center justify-between gap-8">
        <div className="flex items-center gap-3">
           <Logo className="h-8" variant="dark" />
        </div>
        
        <div className="flex items-center gap-8 text-sm font-medium text-orange-600">
            <a href="#" className="hover:text-orange-700 transition-colors">Privacy Policy</a>
            <a href="#" className="hover:text-orange-700 transition-colors">Terms of Service</a>
            <a href="#" className="hover:text-orange-700 transition-colors">Contact Support</a>
        </div>

        <div className="text-xs text-orange-600 font-mono">
            © {new Date().getFullYear()} Electronic Payments & Services Pvt LTD.
        </div>
      </div>
    </footer>
  );
};

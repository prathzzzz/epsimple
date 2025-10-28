import { Logo } from '@/assets/logo'

type AuthLayoutProps = {
  children: React.ReactNode
}

export function AuthLayout({ children }: AuthLayoutProps) {
  return (
    <div className='container grid h-svh max-w-none items-center justify-center'>
      <div className='mx-auto flex w-full flex-col items-center justify-center sm:w-[480px]'>
        <div className='flex h-32 w-full items-center justify-center sm:h-40'>
          <Logo className='mt-30' />
        </div>
        <div className='w-full'>
          {children}
        </div>
      </div>
    </div>
  )
}
import { ContentSection } from '../components/content-section'
import { AccountForm } from './account-form'

export function SettingsAccount() {
  return (
    <ContentSection
      title='Account'
      desc='View your account details, roles, and permissions.'
    >
      <AccountForm />
    </ContentSection>
  )
}
